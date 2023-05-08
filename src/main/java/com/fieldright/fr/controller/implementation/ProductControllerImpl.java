package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.ProductController;
import com.fieldright.fr.entity.Avaliacao;
import com.fieldright.fr.entity.AvaliacaoProduct;
import com.fieldright.fr.entity.ProductFracao;
import com.fieldright.fr.entity.dto.AvaliacaoProductDTO;
import com.fieldright.fr.entity.dto.AvaliacaoProductnNewDTO;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.entity.dto.ProductFracaoDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.security.util.JwtUserUtil;
import com.fieldright.fr.service.interfaces.CSVService;
import com.fieldright.fr.service.interfaces.ProductService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/products/v1")
@CrossOrigin
public class ProductControllerImpl implements ProductController {

    private ProductService productService;

    @Autowired
    private CSVService csvService;

    @Override
    @PostMapping(
            value = "/newProduct"
    )
    public Response<List<ProductDTO>> newProduct(@RequestBody ProductDTO ProductDTO) {
        return productService.newProduct(ProductDTO, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PutMapping(
            value = "/updateProduct"
    )
    public Response<ProductDTO> updateProduct(@RequestBody ProductDTO ProductDTO) {
        return productService.updateProduct(ProductDTO, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @DeleteMapping(
            value = "/deleteProduct"
    )
    public Response<List<ProductDTO>> deleteProduct(@RequestParam long productId) {
        return productService.deleteProduct(productId, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PutMapping(
            value = "/addPictures"
    )
    public Response<ProductDTO> addPictures(@RequestParam long productId, @RequestParam MultipartFile... picture) throws IOException {
        return productService.addPictures(productId, picture, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @DeleteMapping(
            value = "/removePicture"
    )
    public Response<ProductDTO> removePicture(long productId, String pictureUrl) {
        return productService.removePicture(productId, pictureUrl, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @GetMapping(
            value = "/findAll"
    )
    public Response<List<ProductDTO>> findAll() {
        return productService.findAll(JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PostMapping(
            value = "/newProductList"
    )
	public void newProductList(String path) {
		 productService.newProductList(path, JwtUserUtil.getUserAuthenticated()); 
	      
	}
    
    @Override
    @PutMapping(
            value = "/evaluate"
    )
    public ResponseEntity<Response<HttpStatus>> evaluate(@RequestBody AvaliacaoProductnNewDTO avaliacao) {

       AvaliacaoProduct avaliacaoProduct = new AvaliacaoProduct();
       avaliacaoProduct.setProductId(avaliacao.getProductId());
       avaliacaoProduct.setComentario(avaliacao.getComentario());
       avaliacaoProduct.setEstrelas(avaliacao.getEstrelas());
       avaliacaoProduct.setAvaliadorId(avaliacao.getAvaliadorId());

        Response<HttpStatus> response = productService.evaluate(avaliacaoProduct, JwtUserUtil.getUserAuthenticated());
        return new ResponseEntity<>(response, response.getStatus());
    }



    public ResponseEntity<Response<HttpStatus>> evaluate(@RequestBody AvaliacaoProductnNewDTO avaliacao,long lojaId) {

        AvaliacaoProduct avaliacaoProduct = new AvaliacaoProduct();
        avaliacaoProduct.setProductId(avaliacao.getProductId());
        avaliacaoProduct.setComentario(avaliacao.getComentario());
        avaliacaoProduct.setEstrelas(avaliacao.getEstrelas());
        avaliacaoProduct.setAvaliadorId(avaliacao.getAvaliadorId());


        Response<HttpStatus> response = productService.evaluate(avaliacaoProduct,lojaId, JwtUserUtil.getUserAuthenticated());
        return new ResponseEntity<>(response, response.getStatus());
    }

  /*  public ResponseEntity<Response<HttpStatus>> evaluate(@RequestBody AvaliacaoProduct avaliacao, long lojaId) {
        Response<HttpStatus> response = productService.evaluate(avaliacao, lojaId, JwtUserUtil.getUserAuthenticated());
        return new ResponseEntity<>(response, response.getStatus());
    }*/

    @GetMapping(
            value = "/evaluate-productAndUser"
    )
	@Override
	public Response getProductAvaliation(@RequestParam(required = true,name = "productId") long productId,
                                                    @RequestParam(required = true,name = "avaliadorId") long avaliadorId) {

        AvaliacaoProductDTO result=  productService.findAvaliacaoProductByProductAndAvaliador(productId, avaliadorId);

        return  new Response.Builder()
                .withData(result)
                .withStatus(HttpStatus.OK)
                .withErrors(new ArrayList<>())
                .build();

	}

    @GetMapping(
            value = "/evaluate"
    )
    @Override
    public Response getEvaluate(Pageable pageable){

        return productService.findAvaliacaoProduct( pageable);

    }

    @GetMapping(
            value = "/evaluate-product"
    )
    @Override
    public Response getEvaluate(@RequestParam(name = "productId",required = true) Long productId,Pageable pageable){

        return productService.findAvaliacaoProduct(productId, pageable);

    }

	 @Override
	    @PostMapping(
	            value = "/newFracao"
	    )
	public ProductFracaoDTO create(@RequestBody ProductFracaoDTO dto) {
		return productService.newFracao(dto);
	}

	 @Override
	    @GetMapping(
	            value = "/list"
	    )
	public List<String> listFracao(@RequestParam(name = "productId", defaultValue = "0", required = true) long productId) {
		return productService.listFracao(productId);
	}

    @PostMapping(value = "/importProductCsv")
    @Override
    public Response uploadFile(@RequestParam("file") MultipartFile file) {
        return csvService.saveProduct(file);
    }


}
