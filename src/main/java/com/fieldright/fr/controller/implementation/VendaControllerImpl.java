package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.VendaController;
import com.fieldright.fr.entity.dto.VendaDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.security.util.JwtUserUtil;
import com.fieldright.fr.service.interfaces.VendaService;
import com.fieldright.fr.util.exception.PermissionDeniedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/vendas/v1")
public class VendaControllerImpl implements VendaController {

    @Autowired
    private VendaService vendaService;

    @Override
    @PutMapping(
            value = "/confirm",
            params = "id"
    )
    public Response<HttpStatus> confirm(long id) {
        try {
            return vendaService.confirm(id, JwtUserUtil.getUserAuthenticated());
        } catch (RuntimeException e) {
            return returnHttpStatusError(e);
        }
    }

    @Override
    @PutMapping(
            value = "/cancel",
            params = "id"
    )
    public Response<HttpStatus> cancel(long id) {
        try {
            return vendaService.cancel(id, JwtUserUtil.getUserAuthenticated());
        } catch (RuntimeException e) {
            return returnHttpStatusError(e);
        }
    }

    @NotNull
    private Response<HttpStatus> returnHttpStatusError(RuntimeException e) {
        return new Response.Builder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .withData(null)
                .withErrors(Arrays.asList(e.getMessage()))
                .build();
    }

    @Override
    @PutMapping(
            value = "/ready",
            params = "id"
    )
    public Response<VendaDTO> ready(long id) {
        try {
            return vendaService.ready(id, JwtUserUtil.getUserAuthenticated());
        } catch (RuntimeException e) {
            return returnVendaDTOError(e);
        }
    }

    @NotNull
    private Response<VendaDTO> returnVendaDTOError(RuntimeException e) {
        return new Response.Builder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .withData(null)
                .withErrors(Arrays.asList(e.getMessage()))
                .build();
    }

    @Override
    @GetMapping
    public Response<List<VendaDTO>> findAll() {
        try {
            return vendaService.findAll(JwtUserUtil.getUserAuthenticated());
        } catch (RuntimeException e) {
            return returnVendaDTOListError(e);
        }
    }

    @Override
    @PutMapping(
            value = "/pedidoEnviado/{vendaId}"
    )
    public Response pedidoACaminho(@PathVariable Long vendaId) {
        try {
            vendaService.pedidoACaminho(vendaId, JwtUserUtil.getUserAuthenticated());
            return getResponse(HttpStatus.OK, null);
        } catch (PermissionDeniedException e) {
            return getResponse(HttpStatus.FORBIDDEN, Collections.singletonList(e.getMessage()));
        }
    }

    @Override
    @PutMapping(
            value = "/pedidoEntregue/{vendaId}"
    )
    public Response pedidoFinalizado(@PathVariable Long vendaId) {
        try {
            vendaService.pedidoFinalizado(vendaId, JwtUserUtil.getUserAuthenticated());
            return getResponse(HttpStatus.OK, null);
        } catch (PermissionDeniedException e) {
            return getResponse(HttpStatus.FORBIDDEN, Collections.singletonList(e.getMessage()));
        }
    }

    private Response getResponse(HttpStatus status, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(null)
                .withErrors(errors)
                .build();
    }

    private Response<List<VendaDTO>> returnVendaDTOListError(RuntimeException e) {
        return new Response.Builder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .withData(null)
                .withErrors(Arrays.asList(e.getMessage()))
                .build();
    }
}
