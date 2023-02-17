package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.UnidadeMedidaConverter;
import com.fieldright.fr.entity.dto.UnidadeMedidaConverterDTO;
import com.fieldright.fr.response.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UnidadeMedidaConverterService {

    public Response<UnidadeMedidaConverterDTO> save(UnidadeMedidaConverterDTO dto);

    public Response<UnidadeMedidaConverterDTO> update(Long id,UnidadeMedidaConverterDTO dto);

    public Response<UnidadeMedidaConverterDTO> delete(Long id);
    public Response<Page<UnidadeMedidaConverterDTO>> findAll(Pageable pageable);

    public UnidadeMedidaConverter findByUnidadeSimbolo(String unidadeOrigem,String unidadeDestino);
}
