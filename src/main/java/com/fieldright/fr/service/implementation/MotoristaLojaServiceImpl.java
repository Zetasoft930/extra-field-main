package com.fieldright.fr.service.implementation;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fieldright.fr.entity.MotoristaLoja;
import com.fieldright.fr.entity.dto.MotoristaLojaDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.repository.MotoristaLojaRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.MotoristaLojaService;
import com.fieldright.fr.util.mapper.MotoristaLojaMapper;

@Service
public class MotoristaLojaServiceImpl implements MotoristaLojaService {

	@Autowired
	private MotoristaLojaRepository lojaRepository;

	@Autowired
	private MotoristaLojaMapper motoristaLojaMapper;

	@Override
	public Response<HttpStatus> create(MotoristaLoja MotoristaLoja) {
		try {
			motoristaLojaMapper.fromMotoristaLoja(lojaRepository.save(MotoristaLoja));
		} catch (RuntimeException e) {
			return returnHttpStatusResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST,
					Arrays.asList(e.getMessage()));
		}
		return returnHttpStatusResponse(HttpStatus.OK, HttpStatus.CREATED, null);
	}
	
	@Override
	public List<MotoristaLoja> findByMotorista(long motorista) {
		return lojaRepository.findAllLojaByMotorista(motorista);
	}

	private Response<HttpStatus> returnHttpStatusResponse(HttpStatus status, HttpStatus data, List<String> erros) {
		return new Response.Builder().withStatus(status).withData(data).withErrors(erros).build();
	}

	@Override
	public List<MotoristaLoja> findByLoja(long loja) {
		return lojaRepository.findAllMotoristaByLoja(loja);
	}

	@Override
	public List<MotoristaLoja> deleteMotoristaLoja(long id,  UserAuthenticated authenticated) {
		 lojaRepository.deleteById(id);
		 return lojaRepository.findAllLojaByMotorista(authenticated.getId());
	}

}
