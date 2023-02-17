package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.UnidadeMedidaConverterController;
import com.fieldright.fr.entity.dto.UnidadeMedidaConverterDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.UnidadeMedidaConverterService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/unidade-medida-converter/v1")
@CrossOrigin
public class UnidadeMedidaConverterControllerImpl implements UnidadeMedidaConverterController {

    @Autowired
    private UnidadeMedidaConverterService medidaConverterService;

    @PostMapping(value = "/save")
    @Override
    public Response save(@RequestBody @Valid UnidadeMedidaConverterDTO postagemDTO) {
        return medidaConverterService.save(postagemDTO);
    }

    @PutMapping(value = "/update")
    @Override
    public Response update(Long id, UnidadeMedidaConverterDTO postagemDTO) {
        return null;
    }

    @GetMapping(value = "/findAll")
    @Override
    public Response findAll(Pageable pageable) {
        return medidaConverterService.findAll(pageable);
    }

    @DeleteMapping
    @Override
    public Response delete(Long id) {
        return medidaConverterService.delete(id);
    }
}
