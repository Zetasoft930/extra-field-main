package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.dto.ProdutoVendidoDTO;
import com.fieldright.fr.entity.dto.VendaDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.util.enums.StatusVenda;
import com.fieldright.fr.util.exception.PermissionDeniedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

public interface VendaService {

    Response<HttpStatus> confirm(long id, UserAuthenticated authenticated);

    Response<HttpStatus> cancel(long id, UserAuthenticated authenticated);

    Response<VendaDTO> ready(long id, UserAuthenticated authenticated);

    void internalVendaACaminho(long vendaId);

    void internalVendaFinalizada(long vendaId);

    Response<List<VendaDTO>> findAll(UserAuthenticated authenticated);

    void pedidoACaminho(Long vendaId, UserAuthenticated authenticated) throws PermissionDeniedException;

    void pedidoFinalizado(Long vendaId, UserAuthenticated authenticated) throws PermissionDeniedException;


    //NOVO CODIGO
    Response<BigDecimal> countVendaByVendedorAndStatus(long usuarioLojaId, StatusVenda status);

    Response<Page<ProdutoVendidoDTO>> findByProductMasVendido(Long usuarioId, Pageable pageable);
}
