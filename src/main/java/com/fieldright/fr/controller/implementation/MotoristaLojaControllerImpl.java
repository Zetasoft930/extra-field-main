package com.fieldright.fr.controller.implementation;

import java.util.List;

import javax.validation.Valid;

import com.fieldright.fr.entity.dto.HistoricoPagMotoristaDTO;
import com.fieldright.fr.service.interfaces.HistoricoPagamentoService;
import com.fieldright.fr.util.enums.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fieldright.fr.controller.interfaces.MotoristaLojaController;
import com.fieldright.fr.entity.MotoristaLoja;
import com.fieldright.fr.entity.dto.MotoristaLojaDTO;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.security.util.JwtUserUtil;
import com.fieldright.fr.service.interfaces.MotoristaLojaService;
import com.fieldright.fr.util.mapper.MotoristaLojaMapper;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/user-loja/v1")
public class MotoristaLojaControllerImpl implements MotoristaLojaController {
	
	@Autowired
	private MotoristaLojaService motoristaLojaService;
	
	@Autowired
	private MotoristaLojaMapper motoristaLojaMapper;

	@Autowired
	private HistoricoPagamentoService historicoPagamentoService;
	
	@Override
    @PostMapping(
            value = "choose-stores"
    )
	public ResponseEntity<Response<HttpStatus>> create(@RequestBody @Valid MotoristaLojaDTO motoristaLojaDTO) {
		MotoristaLoja MotoristaLoja = motoristaLojaMapper.toMotoristaLoja(motoristaLojaDTO);
		Response<HttpStatus> response = motoristaLojaService.create(MotoristaLoja);
        return new ResponseEntity<>(response, response.getStatus());                     
	}

	 @GetMapping(
	            value = "/findAllByLoja"
	    )
	@Override
	public List<MotoristaLoja> findAllByLojas(long loja) {
		return motoristaLojaService.findByLoja(loja);
	}

	 @GetMapping(
	            value = "/findAllByMotorista"
	    )
	@Override
	public List<MotoristaLoja> findAllByMotorista(long motorista) {
		return motoristaLojaService.findByMotorista(motorista);
	}

	 @DeleteMapping(
	            value = "/delete",
	            params = "id"
	    )
	@Override
	public List<MotoristaLoja> deleteMotoristaLoja(long id) {
		return motoristaLojaService.deleteMotoristaLoja(id, JwtUserUtil.getUserAuthenticated());
	}

	@GetMapping(value = "/historico-pagamento")
	@Override
	public Response findPagamentoByMotorista(@RequestBody HistoricoPagMotoristaDTO dto, Pageable pageable) {
		return historicoPagamentoService.findPagamentoByUsuario(dto, TipoUsuario.MOTORISTA,pageable);
	}


}
