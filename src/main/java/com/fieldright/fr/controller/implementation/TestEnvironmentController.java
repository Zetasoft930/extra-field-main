package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.implementation.CompraServiceImpl;
import com.fieldright.fr.util.exception.AmbienteNaoSuportadoException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/test/v1")
@Api(
                value = "Endpoints de ambiente de teste",
                description = "Os endpoints descritos aqui funcionam apenas em ambiente de teste",
                hidden = true
)
public class TestEnvironmentController {

    @Value("${spring.profiles.active}")
    private String ambiente;
    @Autowired
    private CompraServiceImpl compraService;

    @ApiOperation(
                    value = "Pagar compra",
                    notes = "Este endpoint serve para simular o pagamento de uma compra. Ele funciona apenas em ambiente de teste."
    )
    @PutMapping(
                    value = "/pagarCompra/{carrinhoId}"
    )
    ResponseEntity<Response> pagarCompra(@PathVariable long carrinhoId) {
        Response response;
        try {
            validaAmbiente();
            compraService.compraPaga_TEST(carrinhoId);
            return retornaResponse(HttpStatus.OK, null);
        } catch (AmbienteNaoSuportadoException e) {
            return retornaResponse(HttpStatus.UNAUTHORIZED, Collections.singletonList(e.getMessage()));
        }

    }

    @NotNull private ResponseEntity<Response> retornaResponse(HttpStatus status, List<String> errors) {
        Response response;
        response = new Response.Builder()
                        .withStatus(status)
                        .withData(null)
                        .withErrors(errors)
                        .build();
        return new ResponseEntity<>(response, response.getStatus());
    }

    private void validaAmbiente() throws AmbienteNaoSuportadoException {
        if (!ambiente.equals("localhost") && !ambiente.equals("test")) {
            throw new AmbienteNaoSuportadoException();
        }
    }
}
