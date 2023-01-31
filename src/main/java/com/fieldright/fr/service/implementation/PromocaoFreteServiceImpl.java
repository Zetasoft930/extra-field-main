package com.fieldright.fr.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fieldright.fr.entity.PromocaoFrete;
import com.fieldright.fr.entity.PromocaoProduct;
import com.fieldright.fr.entity.dto.PromocaoFreteDTO;
import com.fieldright.fr.entity.dto.PromocaoProductDTO;
import com.fieldright.fr.repository.PromocaoFreteRepository;
import com.fieldright.fr.repository.PromocaoProductRepository;
import com.fieldright.fr.service.interfaces.PromocaoFreteService;
import com.fieldright.fr.service.interfaces.PromocaoProductService;
import com.fieldright.fr.util.mapper.PromocaoFreteMapper;
import com.fieldright.fr.util.mapper.PromocaoProductMapper;

@Service
public class PromocaoFreteServiceImpl implements PromocaoFreteService {
	
	@Autowired
	private PromocaoFreteRepository promocaoFreteRepository;
	
	@Autowired
	private PromocaoFreteMapper promocaoFreteMapper;

	@Override
	public PromocaoFreteDTO save(PromocaoFrete frete) {
		PromocaoFreteDTO dto = promocaoFreteMapper.fromPromocaoFrete(promocaoFreteRepository.save(frete));
		return dto;
	}

	
	
	
	

}
