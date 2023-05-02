package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.CategoriaPostagem;
import com.fieldright.fr.entity.Postagem;
import com.fieldright.fr.entity.Postagem;
import com.fieldright.fr.entity.dto.PostagemDTO;
import com.fieldright.fr.entity.dto.PostagemDTO;
import com.fieldright.fr.util.enums.StatusPostagem;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Component
public class PostagemMapper  {


    private MapperFacade facade;

    public PostagemMapper() {
        this.facade = new ConfigurableMapper();
    }

    public Postagem toPostagem(PostagemDTO dto){
        return Postagem
                .builder()
                .data(LocalDateTime.now())
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .imagem(dto.getImagem())
                .categoria(CategoriaPostagem
                        .builder()
                        .id(dto.getCategoria())
                        .build())
                .status(StatusPostagem.ACTIVADO)
                .build();
    }

    public PostagemDTO toPostagemDTO(Postagem Postagem){


        return PostagemDTO
                .builder()
                .data(Postagem.getData())
                .id(Postagem.getId())
                .titulo(Postagem.getTitulo())
                .descricao(Postagem.getDescricao())
                .imagem(Postagem.getImagem())
                .categoria(Postagem.getCategoria().getId())
                .comentarios(new HashSet<>())
                .build();
    }

    public List<PostagemDTO> toPostagemDTOS(List<Postagem> PostagemList) {
        return facade.mapAsList(PostagemList, PostagemDTO.class);
    }
}
