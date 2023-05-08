package com.fieldright.fr.controller.implementation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fieldright.fr.controller.interfaces.PromocaoproductController;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.entity.dto.PromocaoProductDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.PromocaoProductService;
import com.fieldright.fr.util.mapper.PromocaoProductMapper;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/promocaoProduct/v1")
@AllArgsConstructor
public class PromocaoProductControllerImpl implements PromocaoproductController{
	
	@Autowired
	private PromocaoProductMapper promocaoProductMapper;
	
	@Autowired
	private PromocaoProductService productService;
	
	@Override
	 @PostMapping(
	            value = "/newPromotion"
	    )
	public PromocaoProductDTO create(@RequestBody PromocaoProductDTO dto) {
		return productService.save(promocaoProductMapper.toPromocaoProduct(dto));
	}

	@Override
	@GetMapping(
	            value = "/promotion"
	    )
	public ResponseEntity<Response<Page<ProductDTO>>> getPromotionByDateAndProduct(
			@RequestParam(name = "productId", defaultValue = "0") final long productId, Pageable pageable) {

		Response<Page<ProductDTO>> response = productService.findPromotionProducts(productId, pageable);
		return new ResponseEntity<>(response, response.getStatus());
	}

	@Override
	@GetMapping(
			value = "/promotion-store"
	)
	public ResponseEntity<Response<Page<ProductDTO>>> getPromotionByDateAndVendedor(
			@RequestParam(name = "vendedorId",defaultValue = "0") long vendedorId,
			Pageable pageable) {

		Response<Page<ProductDTO>> response = productService.findPromotionVededor(vendedorId, pageable);
		return new ResponseEntity<>(response, response.getStatus());
	}
	
}
