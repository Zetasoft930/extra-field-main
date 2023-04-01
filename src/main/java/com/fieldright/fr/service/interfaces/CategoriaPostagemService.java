package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.dto.CategoriaPostagemDTO;
import com.fieldright.fr.response.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoriaPostagemService {

    Response save(CategoriaPostagemDTO dto);
    Response<Page<CategoriaPostagemDTO>> findByStatus(Integer status, Pageable pageable);

    Response update(Long id,CategoriaPostagemDTO dto);
    Response delete(Long id);
    Response updateStatus(Long id,Integer status);

    Response findAll(Pageable pageable);


}
