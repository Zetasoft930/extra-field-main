package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.Venda;
import com.fieldright.fr.entity.dto.EntregaDTO;
import com.fieldright.fr.entity.dto.EntregaDateDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.response.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EntregaService {
    void internalGereNovaEntrega(Venda venda);

    Response<EntregaDTO> aceite(long entregaId, UserAuthenticated authenticated);

    Response<EntregaDTO> aCaminho(long entregaId, UserAuthenticated authenticated);

    Response<EntregaDTO> finalize(long entregaId, UserAuthenticated authenticated);

    Response<List<EntregaDTO>> historico(UserAuthenticated authenticated);

    Response<List<EntregaDTO>> findAllDisponivel(UserAuthenticated authenticated);

    Response rejeita(long entregaId, UserAuthenticated authenticated);
    
    Response<Page<EntregaDateDTO>> findByMotoristaAndDays(UserAuthenticated authenticated, int daysAgo, Pageable pageable);
    
    Response<Page<EntregaDateDTO>> getLastEntregas(UserAuthenticated authenticated, Pageable pageable);
}
