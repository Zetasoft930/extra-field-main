package com.fieldright.fr.controller.interfaces;

import com.fieldright.fr.entity.Avaliacao;
import com.fieldright.fr.entity.AvaliacaoProduct;
import com.fieldright.fr.entity.ProductFracao;
import com.fieldright.fr.entity.dto.AvaliacaoProductDTO;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.entity.dto.ProductFracaoDTO;
import com.fieldright.fr.entity.dto.PromocaoProductDTO;
import com.fieldright.fr.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(
        tags = "Product endpoints",
        description = "Realiza operações com produtos (somente vendedor)"
)
public interface ProductController {

    @ApiOperation(
            value = "New product",
            notes = "Este endpoint deve ser chamado para cadastrar um novo produto no sistema. \n" +
                    "Basta passar no body um **ProductDTO**. Os campos obrigatórios são : \n" +
                    "   * ***name***\n" +
                    "   * ***description***\n" +
                    "   * ***price***\n" +
                    "   * ***quantityAvailable*** \n\n" +
                    "Será retornada a mesma entidade porém com mais informações."
    )
    Response<List<ProductDTO>> newProduct(ProductDTO ProductDTO);

    @ApiOperation(
            value = "Update product",
            notes = "Este endpoint deve ser chamado para atualizar alguma informação de um produto já existente. \n" +
                    "Basta passar no body um **ProductDTO** com apenas os campos que precisam ser atualizados\n" +
                    "O único campo obrigatório é o ***Id***"
    )
    Response<ProductDTO>

    updateProduct(ProductDTO ProductDTO);

    @ApiOperation(
            value = "Delete product",
            notes = "Este endpoint deve ser chamado para deletar algum produto já existente. \n" +
                    "Basta passar no path o id do produto que precisa ser deletado"
    )
    Response<List<ProductDTO>> deleteProduct(long productId);

    @ApiOperation(
            value = "Add pictures",
            notes = "Este endpoint deve ser chamado para adicionar fotos num produto já existente. \n" +
                    "Basta passar no body cada foto com a nomenclatura de ***picture*** no ***form-data*** e o id do produto como ***productId*** (Obrigatório)"
    )
    Response<ProductDTO> addPictures(long productId, MultipartFile... picture) throws IOException;

    @ApiOperation(
            value = "Remove picture",
            notes = "Este endpoint deve ser chamado para deletar alguma foto de algum produto. \n" +
                    "Basta passar no path dois campos obrigatórios :\n" +
                    "   * ***productId*** : *O id do produto*\n" +
                    "   * ***pictureUrl*** : *O link da foto a ser excluída. Esse link é o mesmo usado no css*"
    )
    Response<ProductDTO> removePicture(long productId, String pictureUrl);

    @ApiOperation(
            value = "Find all products",
            notes = "Este endpoint deve ser usado para obter a lista de todos os produtos que o comprador pode comprar. \n" +
                    "O mesmo endpoint pode ser usado pelo vendedor para recuperar todos os seus produtos cadastrados."
    )
    Response<List<ProductDTO>> findAll();

    
	@ApiOperation(value = "New product", notes = "Este endpoint deve ser chamado para cadastrar um novo produto no sistema. \n"
			+ "Basta passar no body um **ProductDTO**. Os campos obrigatórios são : \n" + "   * ***name***\n"
			+ "   * ***description***\n" + "   * ***price***\n" + "   * ***quantityAvailable*** \n\n"
			+ "Será retornada a mesma entidade porém com mais informações.")
	void newProductList(String path);
	
	ResponseEntity<Response<HttpStatus>> evaluate(AvaliacaoProduct avaliacao, long lojaId);
	
	
	AvaliacaoProductDTO getProductAvaliation(long productId, long avaliadorId);
	
	@ApiOperation(value = "Criar fração do produto", notes = "")
	@ResponseStatus(HttpStatus.CREATED)
	ProductFracaoDTO create(ProductFracaoDTO dto);
	
	List<String> listFracao(long productId);
}
