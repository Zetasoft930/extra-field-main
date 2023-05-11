package com.fieldright.fr.controller.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fieldright.fr.entity.dto.DepoimentoDTO;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.entity.dto.PromocaoProductDTO;
import com.fieldright.fr.entity.dto.SuperCategoryDTO;
import com.fieldright.fr.response.Response;

import io.swagger.annotations.ApiOperation;

public interface PromocaoproductController {
	
	 @ApiOperation(
	            value = "Promocaoproduct",
	            notes = ""
	    )
	    @ResponseStatus(HttpStatus.CREATED)
	 PromocaoProductDTO create(PromocaoProductDTO dto);

	@ApiOperation(value = "Get Promotion by product", notes = "Este endpoint deve ser utilizado para recuperar as promoções")
	public ResponseEntity<Response<Page<ProductDTO>>> getPromotionByProduto(final long productId, Pageable pageable);
	 @ApiOperation(value = "Get Promotion by date and product", notes = "Este endpoint deve ser utilizado para recuperar as promoções")
		ResponseEntity<Response<Page<ProductDTO>>> getPromotionByDateAndProduct(long productId, LocalDateTime date_end, LocalDateTime date_start, Pageable pageable);


	@ApiOperation(value = "Get Promotion by date and product", notes = "Este endpoint deve ser utilizado para recuperar as promoções por vendedor")
	public ResponseEntity<Response<Page<ProductDTO>>> getPromotionByDateAndVendedor(  long vendedorId,
			Pageable pageable);
}
