package com.fieldright.fr.service.interfaces;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fieldright.fr.entity.Depoimento;
import com.fieldright.fr.entity.dto.DepoimentoDTO;
import com.fieldright.fr.response.Response;

public interface DepoimentoService {

	 DepoimentoDTO create(Depoimento depoimento);
	 
		Response<Page<DepoimentoDTO>> findByFilter(long usuarioId, String status, LocalDate startdate,
				LocalDate endDate, Pageable pageable);
}
