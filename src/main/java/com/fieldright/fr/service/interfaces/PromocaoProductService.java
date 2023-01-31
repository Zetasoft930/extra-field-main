package com.fieldright.fr.service.interfaces;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fieldright.fr.entity.PromocaoProduct;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.entity.dto.PromocaoProductDTO;
import com.fieldright.fr.response.Response;

public interface PromocaoProductService {
	
	PromocaoProductDTO save(PromocaoProduct product);
	
	Response<Page<ProductDTO>> findPromotionProducts(long productId, Pageable pageable);

}
