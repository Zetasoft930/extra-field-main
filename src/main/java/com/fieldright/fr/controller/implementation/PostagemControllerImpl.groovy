package com.fieldright.fr.controller.implementation

import com.fieldright.fr.controller.interfaces.PostagemController
import com.fieldright.fr.entity.dto.PostagemDTO
import com.fieldright.fr.entity.dto.PostagemFilterDTO
import com.fieldright.fr.response.Response
import com.fieldright.fr.service.interfaces.PostagemService
import lombok.AllArgsConstructor
import org.apache.commons.collections4.Get
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

import javax.validation.Valid

@RestController
@AllArgsConstructor
@RequestMapping("/api/postagem/v1")
@CrossOrigin
class PostagemControllerImpl implements PostagemController{

    @Autowired
    private PostagemService postagemService;

    @PostMapping(value = "/save")
    @Override
    Response save(@RequestBody @Valid PostagemDTO postagemDTO) {
        return postagemService.save(postagemDTO);
    }
@GetMapping(value = "/findBystatus")
    @Override
    Response findBystatus(@RequestParam(required = true) Integer status, Pageable pageable) {
        return postagemService.findByStatus(status,pageable)
    }

    @DeleteMapping(value = "/delete/{id}")
    @Override
    Response delete(@PathVariable(required = true) Long id) {
        return postagemService.delete(id);
    }
@PutMapping(value = "/updateStatus/{id}")
    @Override
    Response updateStatus(@PathVariable(required = true)Long id,@RequestParam(required = true) Integer status) {
        return postagemService.updateStatus(id,status);
    }

    @GetMapping(value = "/findAll")
    @Override
    Response findAll(Pageable pageable) {
        return postagemService.findAll(pageable);
    }
}
