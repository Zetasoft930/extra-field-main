package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.AvaliacaoProduct;
import com.fieldright.fr.entity.Compra;
import com.fieldright.fr.entity.Product;
import com.fieldright.fr.entity.ProductFracao;
import com.fieldright.fr.entity.dto.AvaliacaoProductDTO;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.entity.dto.ProductFracaoDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.response.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ProductService {

    Response<List<ProductDTO>> newProduct(ProductDTO productDTO, UserAuthenticated authenticated);

    Response<ProductDTO> updateProduct(ProductDTO productDTO, UserAuthenticated authenticated);

    Response<List<ProductDTO>> deleteProduct(long id, UserAuthenticated authenticated);

    Response<ProductDTO> addPictures(long productId, MultipartFile[] pictures, UserAuthenticated authenticated) throws IOException;

    Response<ProductDTO> removePicture(long productId, String pictureUrl, UserAuthenticated authenticated);

    Product internalFindById(long id);

    void internalSave(Product product);

    void internalUpdateForNewCompra(long productId, int qtdComprada,String unidadeMedida);

    void internalCanceleReservaProduto(long productId, int qtdComprada,String unidadeMedida);

    void internalUpdateForComprasCanceladas(List<Compra> compras);

    Response<List<ProductDTO>> findAll(UserAuthenticated authenticated);

    Response<Page<ProductDTO>> findByFilters(Long lojaId, String name, String category, Pageable pageable);
    
    void newProductList(String path, UserAuthenticated authenticated);
    
    Response<Page<ProductDTO>> findByFilters(long loja, LocalDate searchDate, String category, String name, BigDecimal price, boolean maisVendido, String status, Pageable pageable);

    Response<HttpStatus> evaluate(AvaliacaoProduct avaliacao, long lojaId, UserAuthenticated authenticated);
    
    AvaliacaoProductDTO findAvaliacaoProductByProductAndAvaliador(long producto, long avaliador);
    
    ProductFracaoDTO newFracao(ProductFracaoDTO dto);
    
    List<String> listFracao(long productId);

    public Product findById(Long id);
}
