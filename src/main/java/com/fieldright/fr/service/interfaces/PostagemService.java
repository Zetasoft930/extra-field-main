package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.dto.PostagemDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.util.enums.StatusPostagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public interface PostagemService {

    public Response save(PostagemDTO dto,MultipartFile file);
    public Response<Page<PostagemDTO>> findByStatus(Integer status, Pageable pageable);

    public Response update(Long id,PostagemDTO dto);
    public Response delete(Long id);
    public Response updateStatus(Long id,Integer status);

    public  Response findAll(Pageable pageable);

}
