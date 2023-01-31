package com.fieldright.fr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import com.fieldright.fr.entity.MotoristaLoja;
import com.fieldright.fr.entity.dto.MotoristaLojaDTO;
import com.fieldright.fr.response.Response;

public interface MotoristaLojaRepository extends JpaRepository<MotoristaLoja, Long> {
	
	List<MotoristaLoja> findAllMotoristaByLoja(long loja);
	
	List<MotoristaLoja> findAllLojaByMotorista(long motorista);

}
