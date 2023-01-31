package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.LojaController;
import com.fieldright.fr.entity.dto.Loja;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.ProductService;
import com.fieldright.fr.service.interfaces.UserService;
import com.fieldright.fr.util.pagination.Paginacao;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping(value = "/api/lojas/v1")

public class LojaControllerImpl implements LojaController {

    private UserService userService;
    private ProductService productService;

    @GetMapping
    @Override 
    public ResponseEntity<Response<Page<Loja>>> getLojas(
    		@RequestParam(name = "name", defaultValue = "%%", required = false) String name,
    		@RequestParam(name = "category", defaultValue = "0", required = false) long category, 
			 Pageable pageable) {
        Response<Page<Loja>> response = userService.getLojas(name, category, pageable);
        return new ResponseEntity<>(response, response.getStatus());

    }

    @GetMapping(
            value = "/products"
    )
    @Override
    public Response<Page<ProductDTO>> getProducts(Long lojaId, String name, String category, Paginacao paginacao) {
        return productService.findByFilters(lojaId, name, category, paginacao.simplePageable());
    }

	@GetMapping(value = "/products/filters")
	@Override
	public ResponseEntity<Response<Page<ProductDTO>>> getProductsByFilters( 
			@RequestParam(name = "loja", defaultValue = "0") final long loja, 
			@RequestParam(name = "searchDate", defaultValue = "#{T(java.time.LocalDate).now()}", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate searchDate, 
			@RequestParam(name = "category", defaultValue = "%%", required = false) String category, 
			@RequestParam(name = "name", defaultValue = "%%", required = false) String name, 
			@RequestParam(name = "price", defaultValue = "0") BigDecimal price, 
			@RequestParam(name = "maisVendido", defaultValue = "false", required = false) boolean maisVendido,
			@RequestParam(name = "status", defaultValue = "CONFIRMED") String status, Pageable pageable) {
		Response<Page<ProductDTO>> response = productService.findByFilters(loja, searchDate, category, name, price, maisVendido, status, pageable);
		return new ResponseEntity<>(response, response.getStatus());
	}
}
