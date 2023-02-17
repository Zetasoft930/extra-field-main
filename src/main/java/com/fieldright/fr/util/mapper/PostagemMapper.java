package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.Postagem;
import com.fieldright.fr.entity.Postagem;
import com.fieldright.fr.entity.dto.PostagemDTO;
import com.fieldright.fr.entity.dto.PostagemDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostagemMapper  {


    private MapperFacade facade;

    public PostagemMapper() {
        this.facade = new ConfigurableMapper();
    }

    public Postagem toPostagem(PostagemDTO dto){
        return facade.map(dto, Postagem.class);
    }

    public PostagemDTO toPostagemDTO(Postagem Postagem){
        return facade.map(Postagem, PostagemDTO.class);
    }

    public List<PostagemDTO> toPostagemDTOS(List<Postagem> PostagemList) {
        return facade.mapAsList(PostagemList, PostagemDTO.class);
    }
}
