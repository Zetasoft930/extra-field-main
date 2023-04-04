package com.fieldright.fr.controller.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fieldright.fr.controller.interfaces.PostagemController;
import com.fieldright.fr.entity.dto.PostagemDTO;
import com.fieldright.fr.entity.dto.PostagemFilterDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.PostagemService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/postagem/v1")
@CrossOrigin
class PostagemControllerImpl implements PostagemController{

    @Autowired
    private PostagemService postagemService;

    @PostMapping(value = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    @Override
    public  Response save(@RequestParam(name = "data") String data, @RequestParam(name = "file",required = true) MultipartFile file) throws JsonProcessingException {


        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(data, new TypeReference<Map<String, Object>>(){});

        PostagemDTO postagemDTO = PostagemDTO
                .builder()
                .titulo((String) map.get("titulo"))
                .descricao((String) map.get("descricao"))
                .categoria(Long.parseLong(String.valueOf(map.get("categoria"))))
                .build();

        System.out.printf("data >> "+data);

        return postagemService.save(postagemDTO,file);
    }
@GetMapping(value = "/findBystatus")
    @Override
public Response findBystatus(@RequestParam(required = true) Integer status, Pageable pageable) {
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
