package com.fieldright.fr.service.interfaces;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fieldright.fr.entity.MotoristaLoja;
import com.fieldright.fr.entity.Venda;
import com.fieldright.fr.entity.dto.MotoristaLojaDTO;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.response.Response;

public interface MotoristaLojaService {
	
	Response<HttpStatus> create(MotoristaLoja MotoristaLoja);
	
	List<MotoristaLoja> findByLoja(long loja);
	
	List<MotoristaLoja> findByMotorista(long motorista);
	
	List<MotoristaLoja> deleteMotoristaLoja(long id,  UserAuthenticated authenticated);

}
