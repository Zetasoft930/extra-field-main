package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.Comentario;
import com.fieldright.fr.entity.dto.ComentarioDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.util.enums.StatusComentario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ComentarioService {

	Response<ComentarioDTO> create(Comentario Comentario);

	Response<ComentarioDTO> updateStatus(Long id, StatusComentario status);
	 
		Response<Page<ComentarioDTO>> findByFilter(long usuarioId, String status, LocalDate startdate,
				LocalDate endDate, Pageable pageable);

    Response findById(Long id);
}
