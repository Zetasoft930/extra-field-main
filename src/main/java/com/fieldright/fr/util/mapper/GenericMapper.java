package com.fieldright.fr.util.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;
import com.google.common.reflect.TypeToken;



@Component
public class GenericMapper <Entity, Dto> {
	
	@SuppressWarnings("serial")
	  private final TypeToken<Entity> entityType = new TypeToken<Entity>(getClass()) {};
	  @SuppressWarnings("serial")
	  private final TypeToken<Dto> dtoType = new TypeToken<Dto>(getClass()) {};

	  private ModelMapper modelMapper;

	  public Entity toEntity(final Dto dto) {
	    return getModelMapper().map(dto, entityType.getType());
	  }

	  public Entity updateEntity(final Entity entity, final Dto dto) {
		  getModelMapper().map(dto, entity);
	    return entity;
	  }

	  public Dto toDto(final Entity entity) {
	    return getModelMapper().map(entity, dtoType.getType());
	  }

	  public List<Dto> toDto(final List<Entity> entities) {
	    return entities.stream().map(it -> toDto(it)).collect(Collectors.toList());
	  }

	public ModelMapper getModelMapper() {
		modelMapper=new ModelMapper();
		return modelMapper;
	}
	 

}
