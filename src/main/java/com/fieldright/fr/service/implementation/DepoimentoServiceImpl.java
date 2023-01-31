package com.fieldright.fr.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fieldright.fr.entity.Depoimento;
import com.fieldright.fr.entity.Product;
import com.fieldright.fr.entity.dto.DepoimentoDTO;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.repository.DepoimentoRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.DepoimentoService;
import com.fieldright.fr.util.enums.StatusDepoimento;
import com.fieldright.fr.util.mapper.DepoimentoMapper;

@Service
public class DepoimentoServiceImpl implements DepoimentoService {

	@Autowired
	private DepoimentoRepository depoimentoRepository;
	
	@Autowired
	private DepoimentoMapper depoimentoMapper;
	@Override
	public DepoimentoDTO create(Depoimento depoimento) {
		return depoimentoMapper.toDepoimentoDTO(depoimentoRepository.save(depoimento));
	}
	//u.first_name, u.last_name, u.avatar , u.dtype, d.created_at, d.comentario, d.status
	@Override
	public Response<Page<DepoimentoDTO>> findByFilter(long usuarioId, String status, LocalDate startdate,
			LocalDate endDate, Pageable pageable) {
		 Page<DepoimentoDTO> dtos=null;
		 Page<Object[]> obj = depoimentoRepository.findByFilters(usuarioId, status, startdate, endDate, pageable);

				 dtos = obj.map(Object -> {
			         return DepoimentoDTO.builder().firstName(Object[0].toString())
			        		 .lastName(Object[1].toString())
			        		 .avatar(Object[2].toString())
			        		 .tipoUsuario(Object[3].toString())
			        		 .createdAt(Object[4].toString())
			        		 .comentario(String.valueOf(Object[5]))
			        		 .status(Object[6].toString()).build();
			        });
			        return new Response.Builder()
			                .withStatus(HttpStatus.OK)
			                .withData(dtos)
			                .withErrors(null)
			                .build();
	}

	

}
