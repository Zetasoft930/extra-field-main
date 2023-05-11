package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Comentario;
import com.fieldright.fr.entity.Usuario;
import com.fieldright.fr.entity.dto.ComentarioDTO;
import com.fieldright.fr.repository.ComentarioRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.ComentarioService;
import com.fieldright.fr.service.interfaces.UserService;
import com.fieldright.fr.util.enums.StatusComentario;
import com.fieldright.fr.util.mapper.ComentarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ComentarioServiceImpl implements ComentarioService {

	@Autowired
	private ComentarioRepository ComentarioRepository;
	
	@Autowired
	private ComentarioMapper ComentarioMapper;

	@Autowired
	private UserService userService;
	@Override
	public Response<ComentarioDTO> create(Comentario Comentario) {
		ComentarioDTO comentarioDTO = ComentarioMapper.toComentarioDTO(ComentarioRepository.save(Comentario));

		return new Response.Builder()
				.withStatus(HttpStatus.OK)
				.withData(comentarioDTO)
				.withErrors(null)
				.build();
	}

	@Override
	public Response<ComentarioDTO> updateStatus(Long id, StatusComentario status) {


	 Optional<Comentario> optional =  ComentarioRepository.findById(id);

	 ComentarioDTO comentarioDTO = null;


	 if(optional.isPresent())
	 {

		 Comentario modelo = optional.get();
		 modelo.setStatus(status);


		modelo =  ComentarioRepository.save(modelo);

		comentarioDTO = ComentarioMapper.toComentarioDTO(modelo);

	 }


		return new Response.Builder()
				.withStatus(HttpStatus.OK)
				.withData(comentarioDTO)
				.withErrors(null)
				.build();


	}

	//u.first_name, u.last_name, u.avatar , u.dtype, d.created_at, d.comentario, d.status
	@Override
	public Response<Page<ComentarioDTO>> findByFilter(long usuarioId, String status, LocalDate startdate,
			LocalDate endDate, Pageable pageable) {
		 Page<ComentarioDTO> dtos=null;
		 Page<Object[]> obj = ComentarioRepository.findByFilters(usuarioId, status, startdate, endDate, pageable);

				 dtos = obj.map(Object -> {
			         return ComentarioDTO.builder().firstName(Object[0].toString())
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

	@Override
	public Response findById(Long id) {

		Optional<Comentario> optional = ComentarioRepository.findById(id);
		ComentarioDTO dto = null;
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		if(optional.isPresent())
		{

			Usuario usuario = userService.internalFindUserById(optional.get().getUsuarioId());

			dto = ComentarioDTO.builder().firstName(usuario.getFirstName())
					.lastName(usuario.getLastName())
					.avatar(usuario.getAvatar())
					.tipoUsuario(usuario.getPerfil())
					.createdAt(optional.get().getCreatedAt().toString())
					.comentario(optional.get().getComentario())
					.id(optional.get().getId())
					.usuarioId(usuario.getId())
					.status(optional.get().getStatus().toString()).build();

			httpStatus = HttpStatus.OK;
		}


		return new Response.Builder()
				.withStatus(httpStatus)
				.withData(dto)
				.withErrors(null)
				.build();
	}




}
