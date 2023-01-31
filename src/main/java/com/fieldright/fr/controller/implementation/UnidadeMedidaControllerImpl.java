package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.UnidadeMedidaController;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.UnidadeMedidaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/unidade/v1")
@CrossOrigin
public class UnidadeMedidaControllerImpl implements UnidadeMedidaController {

    private UnidadeMedidaService service;

    @GetMapping
    @Override public Response<List<String>> get() {
        return new Response.Builder()
                        .withStatus(HttpStatus.OK)
                        .withData(service.findAll())
                        .withErrors(null)
                        .build();
    }
}
