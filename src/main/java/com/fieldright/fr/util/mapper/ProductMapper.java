package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.Product;
import com.fieldright.fr.entity.dto.ProductDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    private MapperFacade facade;

    public ProductMapper() {
        this.facade = new ConfigurableMapper();
    }

    public Product toProduct(ProductDTO dto){
        return facade.map(dto, Product.class);
    }

    public ProductDTO toProductDTO(Product product){
        return facade.map(product, ProductDTO.class);
    }

    public List<ProductDTO> toProductDTOS(List<Product> productList){
        return facade.mapAsList(productList,ProductDTO.class);
    }
}
