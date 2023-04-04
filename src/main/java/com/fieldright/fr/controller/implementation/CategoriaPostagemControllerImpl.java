package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.CategoriaPostagemController;
import com.fieldright.fr.controller.interfaces.PostagemController;
import com.fieldright.fr.entity.dto.CategoriaPostagemDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.CategoriaPostagemService;
import com.fieldright.fr.service.interfaces.PostagemService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/categoria-postagem/v1")
@CrossOrigin
class CategoriaPostagemControllerImpl implements CategoriaPostagemController{

    @Autowired
    private CategoriaPostagemService postagemService;

    @PostMapping(value = "/save")
    @Override
    public  Response save(@RequestBody @Valid CategoriaPostagemDTO CategoriaPostagemDTO) {
        return postagemService.save(CategoriaPostagemDTO);
    }
@GetMapping(value = "/findBystatus")
    @Override
public  Response findBystatus(@RequestParam(required = true) Integer status, Pageable pageable) {
        return postagemService.findByStatus(status,pageable);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Override
    public  Response delete(@PathVariable(required = true) Long id) {
        return postagemService.delete(id);
    }
@PutMapping(value = "/updateStatus/{id}")
    @Override
public  Response updateStatus(@PathVariable(required = true)Long id,@RequestParam(required = true) Integer status) {
        return postagemService.updateStatus(id,status);
    }

    @GetMapping(value = "/findAll")
    @Override
    public Response findAll(Pageable pageable) {
        return postagemService.findAll(pageable);
    }
}
