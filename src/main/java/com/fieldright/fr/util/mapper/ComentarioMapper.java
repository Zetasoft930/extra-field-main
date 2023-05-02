package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.Carrinho;
import com.fieldright.fr.entity.Comentario;
import com.fieldright.fr.entity.dto.CarrinhoDTO;
import com.fieldright.fr.entity.dto.ComentarioDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Component
public class ComentarioMapper extends GenericMapper<Comentario, ComentarioDTO> {

	 private MapperFacade facade;

	    public ComentarioMapper() {
	        this.facade = new ConfigurableMapper();
	    }

	    public ComentarioDTO toComentarioDTO(Comentario Comentario){

	        return facade.map(Comentario,ComentarioDTO.class);
	    }
	public Set<ComentarioDTO> toComentarioDTO(Set<Comentario> comentarios){

		Set<ComentarioDTO> result = new HashSet<>();

		for(Comentario comentario : comentarios){



			result.add(toComentarioDTO(comentario));
		}

		return result;
	}
	    
	    public Comentario fromComentarioDTO(ComentarioDTO ComentarioDTO){
	        return facade.map(ComentarioDTO,Comentario.class);
	    }

	    public List<CarrinhoDTO> tComentarioDTOList(List<Carrinho> carrinhoList){
	        return facade.mapAsList(carrinhoList, CarrinhoDTO.class);
	    }

}
