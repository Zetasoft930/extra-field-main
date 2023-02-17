package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.dto.PostagemDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.util.enums.StatusPostagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public interface PostagemService {

    Response save(PostagemDTO dto);
    Response<Page<PostagemDTO>> findByStatus(Integer status, Pageable pageable);

    Response update(Long id,PostagemDTO dto);
    Response delete(Long id);
    Response updateStatus(Long id,Integer status);

    Response findAll(Pageable pageable);

}
