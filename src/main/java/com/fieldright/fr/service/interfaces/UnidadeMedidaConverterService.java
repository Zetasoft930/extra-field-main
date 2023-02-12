package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.UnidadeMedidaConverter;
import com.fieldright.fr.entity.dto.UnidadeMedidaConverterDTO;
import com.fieldright.fr.response.Response;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UnidadeMedidaConverterService {

    public Response<UnidadeMedidaConverterDTO> save(UnidadeMedidaConverterDTO dto);

    public Response<Page<UnidadeMedidaConverterDTO>> findAll();
   // public Response<UnidadeMedidaConverterDTO> findByUnidadeOrigem(String unidade);

   // public UnidadeMedidaConverter findByUnidadeSimbolo(String unidade);
    public UnidadeMedidaConverter findByUnidadeSimbolo(String unidadeOrigem,String unidadeDestino);
}
