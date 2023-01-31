package com.fieldright.fr.controller.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fieldright.fr.controller.interfaces.DepoimentoController;
import com.fieldright.fr.entity.dto.DepoimentoDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.DepoimentoService;
import com.fieldright.fr.util.enums.StatusDepoimento;
import com.fieldright.fr.util.mapper.DepoimentoMapper;

@RestController
@RequestMapping("/api/depoimento/v1")
@CrossOrigin
public class DepoimentoControllerImpl implements DepoimentoController {

	@Autowired
	private DepoimentoService depoimentoService;
	
	@Autowired
	private DepoimentoMapper depoimentoMapper;
	
	@Override
	 @PostMapping(
	            value = "/newDepoimento"
	    )
	public DepoimentoDTO create(@RequestBody DepoimentoDTO dto) {
		dto.setStatus(StatusDepoimento.PENDING.name());
		return depoimentoService.create(depoimentoMapper.toEntity(dto));
	}

	@GetMapping(value = "/depoimentos/filters")
	@Override
	public ResponseEntity<Response<Page<DepoimentoDTO>>> getDepoimentosByFilters(
			@RequestParam(name = "usuarioId", defaultValue = "0", required = false) final long usuarioId, 
			@RequestParam(name = "status", defaultValue = "%%", required = false) final String status,
			@RequestParam(name = "startDate", defaultValue = "#{T(java.time.LocalDate).now()}", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate startdate, 
			@RequestParam(name = "endDate", defaultValue = "#{T(java.time.LocalDate).now()}", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate endDate, Pageable pageable) {	
	Response<Page<DepoimentoDTO>> response =depoimentoService.findByFilter(usuarioId, status, startdate, endDate, pageable);
	return new ResponseEntity<>(response, response.getStatus());
	}

}
