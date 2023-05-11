package com.fieldright.fr.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fieldright.fr.entity.Product;
import com.fieldright.fr.entity.PromocaoProduct;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.entity.dto.PromocaoProductDTO;
import com.fieldright.fr.repository.ProductRepository;
import com.fieldright.fr.repository.PromocaoProductRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.PromocaoProductService;
import com.fieldright.fr.util.mapper.ProductMapper;
import com.fieldright.fr.util.mapper.PromocaoProductMapper;

@Service
public class PromocaoProductServiceImpl implements PromocaoProductService {
	
	@Autowired
	private PromocaoProductRepository promocaoProductRepository;
	
	@Autowired
	private PromocaoProductMapper promocaoProductMapper;
	
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private ProductServiceImpl productServiceImpl;
    
    @Autowired
	private ProductRepository productRepository;

	@Override
	public PromocaoProductDTO save(PromocaoProduct product) {
		PromocaoProductDTO dto = promocaoProductMapper.fromPromocaoProduct(promocaoProductRepository.save(product));
		return dto;
	}

	@Override
	public Response<Page<ProductDTO>> findPromotionProducts(long productId, Pageable pageable) {
		 Page<Product> products=null;
		 Page<ProductDTO> dtos=null;
		 products = productRepository.findPromotionProducts(productId, pageable);
		 
		 dtos = products.map(product -> {
			 ProductDTO dto= productMapper.toProductDTO(product);
			 dto.setPromotioPrice(productServiceImpl.gePriceToSell(product.getId()));
			 dto.setPercentage(productServiceImpl.gePercentage(product.getId()));
	         return dto;
	        });
	        return new Response.Builder()
	                .withStatus(HttpStatus.OK)
	                .withData(dtos)
	                .withErrors(null)
	                .build();
	}

	@Override
	public Response<Page<ProductDTO>> findPromotionVededor(long vendedor, Pageable pageable) {
		Page<Product> products=null;
		Page<ProductDTO> dtos=null;
		products = productRepository.findPromotionVendedor(vendedor, pageable);

		dtos = products.map(product -> {
			ProductDTO dto= productMapper.toProductDTO(product);
			dto.setPromotioPrice(productServiceImpl.gePriceToSell(product.getId()));
			dto.setPercentage(productServiceImpl.gePercentage(product.getId()));
			return dto;
		});
		return new Response.Builder()
				.withStatus(HttpStatus.OK)
				.withData(dtos)
				.withErrors(null)
				.build();
	}

	@Override
	public Response<Page<ProductDTO>> findPromotionProducts(long productId, LocalDateTime date_start, LocalDateTime date_end, Pageable pageable) {
		Page<Product> products=null;
		Page<ProductDTO> dtos=null;
		products = productRepository.findPromotionProducts(productId,date_start,date_end, pageable);

		dtos = products.map(product -> {
			ProductDTO dto= productMapper.toProductDTO(product);
			dto.setPromotioPrice(productServiceImpl.gePriceToSell(product.getId()));
			dto.setPercentage(productServiceImpl.gePercentage(product.getId()));
			return dto;
		});
		return new Response.Builder()
				.withStatus(HttpStatus.OK)
				.withData(dtos)
				.withErrors(null)
				.build();
	}


}
