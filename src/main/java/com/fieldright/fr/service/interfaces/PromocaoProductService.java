package com.fieldright.fr.service.interfaces;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fieldright.fr.entity.PromocaoProduct;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.entity.dto.PromocaoProductDTO;
import com.fieldright.fr.response.Response;

import java.time.LocalDateTime;

public interface PromocaoProductService {
	
	PromocaoProductDTO save(PromocaoProduct product);
	
	Response<Page<ProductDTO>> findPromotionProducts(long productId, Pageable pageable);


	Response<Page<ProductDTO>> findPromotionVededor(long vendedor, Pageable pageable);

	public Response<Page<ProductDTO>> findPromotionProducts(long productId, LocalDateTime date_start, LocalDateTime date_end, Pageable pageable);

}
