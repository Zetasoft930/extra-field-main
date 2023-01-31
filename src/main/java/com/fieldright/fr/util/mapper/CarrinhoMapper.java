package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.Carrinho;
import com.fieldright.fr.entity.dto.CarrinhoDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarrinhoMapper {

    private MapperFacade facade;

    public CarrinhoMapper() {
        this.facade = new ConfigurableMapper();
    }

    public CarrinhoDTO toCarrinhoDTO(Carrinho carrinho){
        return facade.map(carrinho,CarrinhoDTO.class);
    }

    public List<CarrinhoDTO> toCarrinhoDTOList(List<Carrinho> carrinhoList){
        return facade.mapAsList(carrinhoList, CarrinhoDTO.class);
    }
}
