package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.EntregaController;
import com.fieldright.fr.entity.dto.DepoimentoDTO;
import com.fieldright.fr.entity.dto.EntregaDTO;
import com.fieldright.fr.entity.dto.EntregaDateDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.security.util.JwtUserUtil;
import com.fieldright.fr.service.interfaces.EntregaService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@CrossOrigin
@RequestMapping(
        value = "/api/entregas/v1"
)

public class EntregaControllerImpl implements EntregaController {

    @Autowired
    private EntregaService entregaService;

    @Override
    @PutMapping(
            value = "/aceite",
            params = "entregaId"
    )
    public Response<EntregaDTO> aceite(long entregaId) {
        return entregaService.aceite(entregaId, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PutMapping(
            value = "/recuse",
            params = "entregaId"
    )
    public Response rejeita(long entregaId) {
        return entregaService.rejeita(entregaId, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PutMapping(
            value = "/aCaminho",
            params = "entregaId"
    )
    public Response<EntregaDTO> aCaminho(long entregaId) {
        return entregaService.aCaminho(entregaId, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PutMapping(
            value = "/finalize",
            params = "entregaId"
    )
    public Response<EntregaDTO> finalize(long entregaId) {
        return entregaService.finalize(entregaId, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @GetMapping(
            value = "/historico"
    )
    public Response<List<EntregaDTO>> historico() {
        return entregaService.historico(JwtUserUtil.getUserAuthenticated());
    }
   
  
    @GetMapping(
            value = "/findAllDisponivel"
    )
    @Override
    public Response<List<EntregaDTO>> findAllDisponivel() {
        return entregaService.findAllDisponivel(JwtUserUtil.getUserAuthenticated());
    }

	@Override
	@GetMapping(value = "/historico/filter",
			params = "daysAgo")
	public ResponseEntity<Response<Page<EntregaDateDTO>>> getDepoimentosByFilters(int daysAgo, Pageable pageable) {
		Response<Page<EntregaDateDTO>> response = entregaService
				.findByMotoristaAndDays(JwtUserUtil.getUserAuthenticated(), daysAgo, pageable);
		return new ResponseEntity<>(response, response.getStatus());
	}

	@Override
	@GetMapping(value = "/historico/last")
	public ResponseEntity<Response<Page<EntregaDateDTO>>> getLastEntregasByUser(Pageable pageable) {
		Response<Page<EntregaDateDTO>> response = entregaService
				.getLastEntregas(JwtUserUtil.getUserAuthenticated(), pageable);
		return new ResponseEntity<>(response, response.getStatus());
	}
}
