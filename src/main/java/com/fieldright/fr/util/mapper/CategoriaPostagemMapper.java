package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.CategoriaPostagem;
import com.fieldright.fr.entity.architecture.Category;
import com.fieldright.fr.entity.dto.CategoriaDTO;
import com.fieldright.fr.entity.dto.CategoriaPostagemDTO;
import com.fieldright.fr.util.enums.StatusCategoriPostagem;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoriaPostagemMapper extends GenericMapper<Category, CategoriaDTO> {

    public CategoriaPostagemDTO toCategoriPostagemDTO(CategoriaPostagem p) {


        return CategoriaPostagemDTO
                .builder()
                .id(p.getId())
                .name(p.getName())
                .build();

    }

    public CategoriaPostagem toCategoriPostagem(CategoriaPostagemDTO dto) {


        return CategoriaPostagem
                .builder()
                .id(dto.getId())
                .status(StatusCategoriPostagem.ACTIVADO)
                .name(dto.getName())
                .data(LocalDateTime.now())
                .build();

    }
}
