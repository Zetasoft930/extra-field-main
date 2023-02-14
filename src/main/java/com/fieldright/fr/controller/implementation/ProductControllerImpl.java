package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.ProductController;
import com.fieldright.fr.entity.Avaliacao;
import com.fieldright.fr.entity.AvaliacaoProduct;
import com.fieldright.fr.entity.ProductFracao;
import com.fieldright.fr.entity.dto.AvaliacaoProductDTO;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.entity.dto.ProductFracaoDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.security.util.JwtUserUtil;
import com.fieldright.fr.service.interfaces.CSVService;
import com.fieldright.fr.service.interfaces.ProductService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<Response<HttpStatus>> evaluate(@RequestBody AvaliacaoProduct avaliacao, long lojaId) {
        Response<HttpStatus> response = productService.evaluate(avaliacao, lojaId, JwtUserUtil.getUserAuthenticated());
        return new ResponseEntity<>(response, response.getStatus());
    }

	@Override
	public AvaliacaoProductDTO getProductAvaliation(long productId, long avaliadorId) {
		return productService.findAvaliacaoProductByProductAndAvaliador(productId, avaliadorId);
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
