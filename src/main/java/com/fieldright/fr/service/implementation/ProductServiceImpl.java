package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.*;
import com.fieldright.fr.entity.dto.AvaliacaoProductDTO;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.entity.dto.ProductFracaoDTO;
import com.fieldright.fr.entity.dto.ProdutoVendidoDTO;
import com.fieldright.fr.entity.dto.avaliacao.AvaliadorDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.repository.AvaliacaoProductRepository;
import com.fieldright.fr.repository.ProductFracaoRepository;
import com.fieldright.fr.repository.ProductRepository;
import com.fieldright.fr.repository.PromocaoProductRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.security.util.JwtUserUtil;
import com.fieldright.fr.service.interfaces.*;
import com.fieldright.fr.util.StringUtil;
import com.fieldright.fr.util.enums.StatusProduct;
import com.fieldright.fr.util.exception.AmbienteNaoSuportadoException;
import com.fieldright.fr.util.exception.CategoryNotExistException;
import com.fieldright.fr.util.mapper.ProductAvaliacaoMapper;
import com.fieldright.fr.util.mapper.ProductFracaoMapper;
import com.fieldright.fr.util.mapper.ProductMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.hibernate.type.ForeignKeyDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String UPDATE_PERMISSION_DENIED_MESSAGE = "Este usuário não possui permissão para editar este produto!";
    private static final String PRODUCT_NOT_FOUND_BY_ID_MESSAGE = "Não foi encontrado nenhum produto com o id: ";
    private static final String CATEGORY_NOT_EXISTE_MESSAGE = "A categoria do produto informardo não existe! Informe uma categoria válida";
    @Value("${src.images.url}")
    private String imageUrl;
    @Value("${src.images.past}")
    private String imagesPast;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UnidadeMedidaService unidadeMedidaService;
    @Autowired
    private AvaliacaoProductRepository avaliacaoProductRepository;
    @Autowired
    private PromocaoProductRepository promocaoProductRepository;
    @Autowired
    private ProductAvaliacaoMapper avaliacaoMapper;
    @Autowired
    private ProductFracaoRepository fracaoRepository;
    @Autowired
    private ProductFracaoMapper fracaoMapper;

    @Autowired
    private UnidadeMedidaConverterService unidadeMedidaConverterService;

    /**
     * * Salvar o produto
     * * Recuperar o usuário pelo authenticated
     * * Conferir o perfil do usuário (deve ser vendedor)
     * * Adicionar o produto salvo na lista dos produtos do usuário
     * * Salvar usuário
     * * Retornar a nova lista de produtos do usuário
     *
     * @param productDTO
     * @param authenticated
     * @return
     */
    @Override
    public Response<List<ProductDTO>> newProduct(ProductDTO productDTO, UserAuthenticated authenticated) {
    	//newProductList();
        Usuario vendedor = userService.internalFindUserById(authenticated.getId());
        if (vendedor.getClass().equals(Vendedor.class)) {
        	if(productRepository.findByNameUserAndUnidade(productDTO.getName().trim().toLowerCase(), productDTO.getUnidadeMedida().trim().toLowerCase(),vendedor.getId()) == 0) {
            try {
                categoryService.internalValidCategory(productDTO.getCategory());
                productDTO.setVendedorId(vendedor.getId());
                productDTO.setVendedorName(vendedor.getFullName());
                productDTO.setPesoCubado(calculePesoCubado(productDTO));
                productDTO.setStatus(StatusProduct.PENDING);

                Product toProduct = productMapper.toProduct(productDTO);
                toProduct.setCountry(((Vendedor) vendedor).getCountry());
                toProduct.setEnderecoLoja(((Vendedor) vendedor).getEndereco());

                List<Product> productList = userService.addNewProductInVendedor(productRepository.save(toProduct), (Vendedor) vendedor);

                return returnProductListResponse(HttpStatus.CREATED, productMapper.toProductDTOS(productList), null);
            } catch (CategoryNotExistException e) {
                return returnProductListResponse(HttpStatus.NOT_ACCEPTABLE, null, Arrays.asList(CATEGORY_NOT_EXISTE_MESSAGE));
            }
        }else {
        	  throw new RuntimeException("Produto já existente");
        }
        }
        return returnProductListResponse(HttpStatus.NOT_ACCEPTABLE, null, Arrays.asList("Este usuário não é vendedor, " +
                "portanto não pode cadastrar um produto!"));
    }

    private double calculePesoCubado(Product product) {
        return calculePesoCubado(productMapper.toProductDTO(product));
    }

    /**
     * peso cubado = Peso x Comprimento x Largura x Altura x Fator cubagem
     * Fator cubagem:
     * *    aéreo: 166,7 kg
     * *    marítimo: 1.000 kg
     * *    Rodoviária: 300 kg
     *
     * @param product
     */
    private double calculePesoCubado(ProductDTO product) {
        double comprimento = product.getComprimento() / 100;
        double largura = product.getLargura() / 100;
        double altura = product.getAltura() / 100;
        double pesoCubado = product.getPeso() * comprimento * largura * altura * 300;
        BigDecimal bigDecimal = new BigDecimal(pesoCubado).round(new MathContext(3, RoundingMode.HALF_UP));

        return bigDecimal.doubleValue();
    }

    /**
     * * Confere se o existe algum produto com o id passado
     * * Recuperar o usuário pelo authenticated
     * * Confere se o produto passado pertence ao usuário recuperado
     * * Atuallizar as informações do produto
     * * Salvar o produto
     * * Retornar o produto salvo
     *
     * @param productDTO
     * @param authenticated
     * @return
     */
    @Override
    public Response<ProductDTO> updateProduct(ProductDTO productDTO, UserAuthenticated authenticated) {

        Optional<Product> product = productRepository.findById(productDTO.getId());
        Usuario usuario = userService.internalFindUserById(authenticated.getId());

        if (product.isPresent()) {
            Product oldProduct = product.get();
            if (usuario.getClass().equals(Vendedor.class) && ((Vendedor) usuario).getProducts().contains(oldProduct)) {
                updateInfos(productMapper.toProduct(productDTO), oldProduct);
                productRepository.save(oldProduct);
                return returnProductResponse(HttpStatus.OK, productMapper.toProductDTO(oldProduct), null);
            }
            return returnProductResponse(HttpStatus.BAD_REQUEST, null, Arrays.asList(UPDATE_PERMISSION_DENIED_MESSAGE));
        }
        return returnProductResponse(HttpStatus.BAD_REQUEST, null, Arrays.asList(PRODUCT_NOT_FOUND_BY_ID_MESSAGE + productDTO.getId()));
    }

    /**
     * @param newProduct
     * @param oldProduct
     */
    private void updateInfos(Product newProduct, Product oldProduct) {
        String name = newProduct.getName() == null ? oldProduct.getName() : newProduct.getName();
        String description = newProduct.getDescription() == null ? oldProduct.getDescription() : newProduct.getDescription();
        BigDecimal price = newProduct.getPrice() == null ? oldProduct.getPrice() : newProduct.getPrice();
        BigDecimal quantityAvailable = newProduct.getQuantityAvailable() == null ? oldProduct.getQuantityAvailable() : newProduct.getQuantityAvailable();
        String category = getCategoriaValida(newProduct.getCategory(), oldProduct.getCategory());
        String unidadeMedida = getUnidadeMedidaValida(newProduct.getUnidadeMedida(), oldProduct.getUnidadeMedida());
        Integer min_stock = newProduct.getMin_stock() == null ? oldProduct.getMin_stock() : newProduct.getMin_stock();
        StatusProduct status = newProduct.getStatus() == null ? oldProduct.getStatus() : newProduct.getStatus();
        
        oldProduct.setName(name);
        oldProduct.setDescription(description);
        oldProduct.setPrice(price);
        oldProduct.setQuantityAvailable(quantityAvailable);
        oldProduct.setTpPreparacaoDias(newProduct.getTpPreparacaoDias());
        oldProduct.setTpPreparacaoHoras(newProduct.getTpPreparacaoHoras());
        oldProduct.setTpPreparacaoMinutos(newProduct.getTpPreparacaoMinutos());
        oldProduct.setPeso(newProduct.getPeso());
        oldProduct.setAltura(newProduct.getAltura());
        oldProduct.setLargura(newProduct.getLargura());
        oldProduct.setComprimento(newProduct.getComprimento());
        oldProduct.setPesoCubado(calculePesoCubado(oldProduct));
        oldProduct.setCategory(category);
        oldProduct.setMetrica(newProduct.getMetrica());
        oldProduct.setUnidadeMedida(unidadeMedida);
        oldProduct.setMin_stock(min_stock);
        oldProduct.setStatus(status);
    }

    private String getUnidadeMedidaValida(String newUnidadeMedida, String oldUnidadeMedida) {
        return unidadeMedidaService.isValidUnidadeMedida(newUnidadeMedida) ? newUnidadeMedida : oldUnidadeMedida;
    }

    private String getCategoriaValida(String newCategory, String oldCategory) {
        return categoryService.isValidCategory(newCategory) ? newCategory : oldCategory;
    }

    /**
     * * Confere se o existe algum produto com o id passado
     * * Recuperar o usuário pelo authenticated
     * * Confere se o produto passado pertence ao usuário recuperado
     * * remover o produto da lista dos produtos do usuário
     * * salvar o usuário
     * * excluir o produto no banco de dados
     * * Retornar a lista dos produtos do usuário
     *
     * @param id
     * @param authenticated
     * @return
     */
    @Override
    public Response<List<ProductDTO>> deleteProduct(long id, UserAuthenticated authenticated) {

        Optional<Product> product = productRepository.findById(id);
        Usuario usuario = userService.internalFindUserById(authenticated.getId());

        if (product.isPresent()) {
            Product oldProduct = product.get();
            if (usuario.getClass().equals(Vendedor.class) && ((Vendedor) usuario).getProducts().contains(oldProduct)) {
                ((Vendedor) usuario).getProducts().remove(oldProduct);
                userService.internalUpdateUser(usuario);
                productRepository.deleteById(id);
                return returnProductListResponse(HttpStatus.OK, productMapper.toProductDTOS(((Vendedor) usuario).getProducts()), null);
            }
            return returnProductListResponse(HttpStatus.BAD_REQUEST, null, Arrays.asList(UPDATE_PERMISSION_DENIED_MESSAGE));
        }
        return returnProductListResponse(HttpStatus.BAD_REQUEST, null, Arrays.asList(PRODUCT_NOT_FOUND_BY_ID_MESSAGE + id));
    }

    /**
     * * Recuperar o produto do banco de dados
     * * Recuperar o usuário pelo id do authenticated
     * * Conferir se o usuário é proprietário do produto
     * * Salvar as imagens
     * * Adicionar os links das imagens na lista das fotos do produto
     * * Salvar o produto
     * * Retornar o produto salvo
     *
     * @param productId
     * @param pictures
     * @param authenticated
     * @return
     * @throws IOException
     */
    @Override
    public Response<ProductDTO> addPictures(long productId, MultipartFile[] pictures, UserAuthenticated authenticated) throws IOException {

        Optional<Product> product = productRepository.findById(productId);
        Usuario usuario = userService.internalFindUserById(authenticated.getId());

        if (product.isPresent()) {
            if (usuario.getClass().equals(Vendedor.class) && ((Vendedor) usuario).getProducts().contains(product.get())) {
                for (MultipartFile picture : pictures) {
                    long pictureId = pictureService.updatePicture(picture, null);
                    product.get().getPictures().add(imageUrl + pictureId);
                }
                productRepository.save(product.get());
                return returnProductResponse(HttpStatus.OK, productMapper.toProductDTO(product.get()), null);
            }
            return returnProductResponse(HttpStatus.BAD_REQUEST, null, Arrays.asList(UPDATE_PERMISSION_DENIED_MESSAGE));
        }
        return returnProductResponse(HttpStatus.BAD_REQUEST, null, Arrays.asList(PRODUCT_NOT_FOUND_BY_ID_MESSAGE + productId));
    }

    /**
     * * Recuperar o produto pelo id
     * * Recuperar o usuário pelo id do authenticated
     * * Conferir se o usuário é vendedor e é proprietário do produto
     * * Excluir o pictureUrl da lista de fotos do protudo
     * * Excluir a Picture do banco de dados (PictureService)
     * * Salvar o produto
     *
     * @param productId
     * @param pictureUrl
     * @param authenticated
     * @return
     */
    @Override
    public Response<ProductDTO> removePicture(long productId, String pictureUrl, UserAuthenticated authenticated) {

        Optional<Product> product = productRepository.findById(productId);
        Usuario usuario = userService.internalFindUserById(authenticated.getId());

        if (product.isPresent()) {
            if (usuario.getClass().equals(Vendedor.class) && ((Vendedor) usuario).getProducts().contains(product.get())) {
                product.get().getPictures().remove(pictureUrl);
                String stringId = StringUtil.getTextAfterElement(pictureUrl, imagesPast);
                pictureService.deletePicture(Long.parseLong(stringId));
                productRepository.save(product.get());
                return returnProductResponse(HttpStatus.OK, productMapper.toProductDTO(product.get()), null);
            }
            return returnProductResponse(HttpStatus.BAD_REQUEST, null, Arrays.asList(UPDATE_PERMISSION_DENIED_MESSAGE));
        }
        return returnProductResponse(HttpStatus.BAD_REQUEST, null, Arrays.asList(PRODUCT_NOT_FOUND_BY_ID_MESSAGE + productId));
    }

    @Override
    public Product internalFindById(long id) {
    	Product product = productRepository.findById(id).get();
    	product.setPrice(gePriceToSell(id));
        return product;
    }

    @Override
    public void internalSave(Product product) {
        productRepository.save(product);
    }

    @Override
    public void internalUpdateForNewCompra(long productId, int qtdComprada,String unidadeMedidaCompra) {
        Product product = productRepository.findById(productId).get();

        BigDecimal novaQtdCompra = getQtCompraCalc(product.getUnidadeMedida(),unidadeMedidaCompra,qtdComprada);

        product.setQtdReservada(product.getQtdReservada().subtract(novaQtdCompra));
        product.setQuantityAvailable(product.getQuantityAvailable().subtract(novaQtdCompra));

        productRepository.save(product);
    }
    public BigDecimal getQtCompraCalc(String unidadeProduto,String unidadeCompra,int qtdComprada){


        UnidadeMedidaConverter unidadeMedidaConverter =	unidadeMedidaConverterService.findByUnidadeSimbolo(unidadeCompra,unidadeProduto);

        if(unidadeMedidaConverter != null){

            return unidadeMedidaConverter.getEquivale().multiply(BigDecimal.valueOf(qtdComprada));

        }

        return BigDecimal.valueOf(qtdComprada);

    }
    @Override
    public void internalCanceleReservaProduto(long productId, int qtdComprada,String unidadeMedidaCompra) {
        Product product = productRepository.findById(productId).get();

        BigDecimal novaQtdCompra = getQtCompraCalc(product.getUnidadeMedida(),unidadeMedidaCompra,qtdComprada);
        product.setQtdReservada(product.getQtdReservada().subtract(novaQtdCompra));
        productRepository.save(product);
    }

    /**
     * Este método deve ser chamado após o canelamento de alguma compra.
     * Para cada compra:
     * * Recuperar o produto comprado
     * * Atualizar a quantidade disponível no estoque
     *
     * @param compras
     */
    @Override
    public void internalUpdateForComprasCanceladas(List<Compra> compras) {
        for (Compra compra : compras) {
            Product product = productRepository.findById(compra.getProductId()).get();
            product.setQuantityAvailable(product.getQuantityAvailable().add(BigDecimal.valueOf(compra.getQtdComprada())));
            productRepository.save(product);
        }
    }

    @Override
    public Response<List<ProductDTO>> findAll(UserAuthenticated authenticated) {
        List<ProductDTO> productDTOS;
        Usuario usuario = userService.internalFindUserById(authenticated.getId());

        if (usuario.getPerfil().equalsIgnoreCase("VENDEDOR")) {
            Vendedor vendedor = (Vendedor) usuario;
            productDTOS = productMapper.toProductDTOS(vendedor.getProducts());
        } else
            productDTOS = productMapper.toProductDTOS(productRepository.findAll());
        return returnProductListResponse(HttpStatus.OK, productDTOS, null);
    }

    @Override
    public Response<Page<ProductDTO>> findByFilters(Long lojaId, String name, String category, Pageable pageable) {
        category = category == null || category.isBlank() ? "" : category;
        name = name == null || name.isBlank() ? "" : name;
        Page<Product> products;

        if (lojaId == null) {
            products = productRepository.findByFilters(name.toLowerCase(Locale.ROOT), category.toLowerCase(Locale.ROOT), pageable);
        } else {
            products = productRepository.findByFilters(lojaId, name.toLowerCase(Locale.ROOT), category.toLowerCase(Locale.ROOT), pageable);
        }
        Page<ProductDTO> dtos = products.map(product -> {
            return productMapper.toProductDTO(product);
        });

        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(dtos)
                .withErrors(null)
                .build();
    }

    private Response<List<ProductDTO>> returnProductListResponse(HttpStatus status, List<ProductDTO> products, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(products)
                .withErrors(errors)
                .build();
    }

    private Response<ProductDTO> returnProductResponse(HttpStatus status, ProductDTO productDTO, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(productDTO)
                .withErrors(errors)
                .build();
    }
    
	public void newProductList(String path, UserAuthenticated authenticated) {
		File ficheiro = new File("C:\\Users\\pc\\Documents\\FIELD\\testee.csv");
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
		ProductDTO productDTO=new ProductDTO();
		CSVReader csvReader = null;
		String[] headres = null;
		try {
			csvReader = new CSVReader(new FileReader(ficheiro));
			headres = csvReader.readNext();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CsvValidationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			String[] column = null;
			while ((column = csvReader.readNext()) != null) {
				Map<String, String> fields = new HashMap<String, String>();
				for (int i = 0; i < column.length; i++) {
					fields.put(headres[i], column[i]);
				}
				rows.add(fields);
			}
			rows.forEach(colunas -> {

				productDTO.setAltura(Double.parseDouble(colunas.get("altura")));
				productDTO.setCategory(colunas.get("category"));
				productDTO.setComprimento(Double.parseDouble(colunas.get("comprimento")));
				productDTO.setCountry(colunas.get("country"));
				productDTO.setDescription(colunas.get("description"));
				productDTO.setLargura(Double.parseDouble(colunas.get("largura")));
				productDTO.setMetrica(Double.parseDouble(colunas.get("metrica")));
				productDTO.setName(colunas.get("name"));
				productDTO.setPeso(Double.parseDouble(colunas.get("peso")));
				productDTO.setPesoCubado(Double.parseDouble(colunas.get("pesoCubado")));
				productDTO.setPrice(new BigDecimal((colunas.get("price"))));
				productDTO.setQuantityAvailable(Integer.parseInt(colunas.get("quantityAvailable")));
				productDTO.setTpPreparacaoDias(Integer.parseInt(colunas.get("tpPreparacaoDias")));
				productDTO.setTpPreparacaoHoras(Integer.parseInt(colunas.get("tpPreparacaoHoras")));
				productDTO.setTpPreparacaoMinutos(Integer.parseInt(colunas.get("tpPreparacaoMinutos")));
				productDTO.setUnidadeMedida(colunas.get("unidadeMedida"));
				productDTO.setMin_stock(Integer.parseInt(colunas.get("min_stock")));
				newProduct(productDTO, authenticated);

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Response<Page<ProductDTO>> findByFilters(long loja, LocalDate searchDate, String category, String name, BigDecimal price, boolean maisVendido,  String status, Pageable pageable) {
		 Page<ProductDTO> dtos=null;
		 Page<Product> products=null;
		
		if(maisVendido) {
			products = productRepository.findMaisVendidosFilters(loja, searchDate, category, name, price, status, pageable);
		}else{
		
		 products = productRepository.findByFilters(loja, searchDate, category, name, price, status, pageable);
		} 
		 
		  dtos = products.map(product -> {
			 ProductDTO dto= productMapper.toProductDTO(product);
			 dto.setPromotioPrice(gePriceToSell(product.getId()));
			 dto.setPercentage(gePercentage(product.getId()));
	         return dto;
	        });
	        return new Response.Builder()
	                .withStatus(HttpStatus.OK)
	                .withData(dtos)
	                .withErrors(null)
	                .build();
	}

	@Override
	public Response<HttpStatus> evaluate(AvaliacaoProduct avaliacao, long lojaId, UserAuthenticated authenticated) {
		/**
		 * * Só comprador pode avaliar * Só pode ter entre 0 - 5 estrelas
		 */
		Usuario comprador = userService.internalFindUserById(authenticated.getId());
        Vendedor loja = null;
		/*try
        {
            loja = (Vendedor) userService.internalFindUserById(lojaId);
        }catch (Exception e)
        {

        }*/
		List<String> erros = new ArrayList<>();

		if (comprador.getClass().equals(Comprador.class)) {
			if (avaliacao.getEstrelas() <= 5 && avaliacao.getEstrelas() >= 0) {
				avaliacao.setAvaliadorId(authenticated.getId());
			avaliacaoProductRepository.save(avaliacao);
            /*if(loja != null) {
                userService.internalUpdateUser(loja);
            }*/
				return returnHttpStatusInResponse(HttpStatus.OK, null);
			} else {
				erros.add("A quantidade de estrelas passada não é vpalida");
			}
		} else {
			erros.add("Avaliações são permitidas somente para compradores");
		}
		return returnHttpStatusInResponse(HttpStatus.UNAUTHORIZED, erros);
	}
    @Override
    public Response<HttpStatus> evaluate(AvaliacaoProduct avaliacao, UserAuthenticated authenticated) {
        /**
         * * Só comprador pode avaliar * Só pode ter entre 0 - 5 estrelas
         */
        Usuario comprador = userService.internalFindUserById(authenticated.getId());

        List<String> erros = new ArrayList<>();

        if (comprador.getClass().equals(Comprador.class)) {
            if (avaliacao.getEstrelas() <= 5 && avaliacao.getEstrelas() >= 0) {
                avaliacao.setAvaliadorId(authenticated.getId());
                avaliacaoProductRepository.save(avaliacao);

                return returnHttpStatusInResponse(HttpStatus.OK, null);
            } else {
                erros.add("A quantidade de estrelas passada não é vpalida");
            }
        } else {
            erros.add("Avaliações são permitidas somente para compradores");
        }
        return returnHttpStatusInResponse(HttpStatus.UNAUTHORIZED, erros);
    }
	
	private Response<HttpStatus> returnHttpStatusInResponse(HttpStatus status, List<String> errors) {
		return new Response.Builder().withStatus(status).withData(status).withErrors(errors).build();
	}
	@Override
	public AvaliacaoProductDTO findAvaliacaoProductByProductAndAvaliador(long producto, long avaliador) {
		return avaliacaoMapper.toProductAvaliacaoDTO(avaliacaoProductRepository.findAvaliacaoProductByProductAndAvaliador(producto, avaliador));
	}
	
	public PromocaoProduct getPromocaoProduct(long id) {
		PromocaoProduct p = null;
		try {
		p = promocaoProductRepository.findproductPromotion(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public BigDecimal gePriceToSell(long id) {
		BigDecimal value;
		BigDecimal price;
		try {
			price = productRepository.findById(id).get().getPrice();
			PromocaoProduct p = getPromocaoProduct(id);
			value = p != null ? p.getPercentage() : BigDecimal.ZERO;
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
		return price.subtract(price.multiply(value != null ? value : BigDecimal.ZERO));
	}

	public BigDecimal gePercentage(long id) {
		try {
			PromocaoProduct p = getPromocaoProduct(id);
			return p != null ? p.getPercentage() : BigDecimal.ZERO;
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	@Override
	public ProductFracaoDTO newFracao(ProductFracaoDTO productFracaoDTO) {
		ProductFracao entity = fracaoMapper.toProductFracao(productFracaoDTO);
		ProductFracao productFracao = fracaoRepository.save(entity);
		return fracaoMapper.toProductFracaoDTO(productFracao);
	}
	
	public int getLimiteFracao(long productId) {
		return fracaoRepository.findFracaoByProduct(productId);
	}

	@Override
	public List<String> listFracao(long productId) {
		List<String> list = new ArrayList<>();
		int limit = getLimiteFracao(productId);
		for (int i = 1; i <= limit; i++) {
			list.add(String.valueOf(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(i), 2, RoundingMode.HALF_UP)));
		}
		return list;
	}

    @Override
    public Product findById(Long id) {
        Optional<Product> product =  productRepository.findById(id);
        if(product.isPresent()){
            return product.get();
        }
        throw new RuntimeException("Produto nao foi encontrado");
    }

    @Transactional
    @Override
    public void internalSave(List<Product> products) {

       for(Product p : products)
           {
              p = productRepository.save(p);
           }
    }

    @Override
    public List<Product> findStockEmBaixoByUserLoja() {
        return productRepository.findStockEmBaixoByUserLoja();
    }

    @Override
    public Response findAvaliacaoProduct(Pageable pageable) {



        Page<Object[]> objs = avaliacaoProductRepository.findAvaliacaoAll(pageable);
        Page<com.fieldright.fr.entity.dto.avaliacao.AvaliacaoProductDTO> dtos = null;
        dtos = objs.map(Object -> {


            Optional<Product> product = productRepository.findById(Long.parseLong(Object[0].toString()));


            com.fieldright.fr.entity.dto.avaliacao.AvaliacaoProductDTO result =  com.fieldright.fr.entity.dto.avaliacao.AvaliacaoProductDTO
                    .builder()
                    .productName(product.get().getName())
                    .productId(product.get().getId())
                    .estrela(BigDecimal.valueOf(Double.valueOf(String.valueOf(Object[1]))))
                    .avaliadores(new HashSet<>())
                    .build();

            List<Object[]> objsAvaliador = avaliacaoProductRepository.findUserByProuct(product.get().getId());

            for(Object[] objects : objsAvaliador){

                result.getAvaliadores().add(AvaliadorDTO
                        .builder()
                                .id(Long.parseLong(String.valueOf(objects[0])))
                                .firstName(String.valueOf(objects[1]))
                                .lastName(String.valueOf(objects[2]))
                                .avatar(String.valueOf(objects[3]))
                                .estrela(BigDecimal.valueOf(Double.parseDouble(String.valueOf(objects[4]))))
                        .build());

            }

            return  result;

        });
        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(dtos)
                .withErrors(null)
                .build();



    }

    @Override
    public Response findAvaliacaoProduct(Long productId, Pageable pageable) {

        Page<Object[]> objs = avaliacaoProductRepository.findAvaliacaoByProduto(productId,pageable);
        Page<com.fieldright.fr.entity.dto.avaliacao.AvaliacaoProductDTO> dtos = null;
        dtos = objs.map(Object -> {


            Optional<Product> product = productRepository.findById(Long.parseLong(Object[0].toString()));

            com.fieldright.fr.entity.dto.avaliacao.AvaliacaoProductDTO result =  com.fieldright.fr.entity.dto.avaliacao.AvaliacaoProductDTO
                    .builder()
                    .productName(product.get().getName())
                    .productId(product.get().getId())
                    .estrela(BigDecimal.valueOf(Double.valueOf(String.valueOf(Object[1]))))
                    .avaliadores(new HashSet<>())
                    .build();

            List<Object[]> objsAvaliador = avaliacaoProductRepository.findUserByProuct(product.get().getId());

            for(Object[] objects : objsAvaliador){

                result.getAvaliadores().add(AvaliadorDTO
                        .builder()
                        .id(Long.parseLong(String.valueOf(objects[0])))
                        .firstName(String.valueOf(objects[1]))
                        .lastName(String.valueOf(objects[2]))
                        .avatar(String.valueOf(objects[3]))
                        .estrela(BigDecimal.valueOf(Double.parseDouble(String.valueOf(objects[4]))))
                        .build());

            }

            return result;

        });
        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(dtos)
                .withErrors(null)
                .build();
    }

}
