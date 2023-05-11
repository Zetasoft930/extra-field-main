package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.CompraController;
import com.fieldright.fr.entity.Avaliacao;
import com.fieldright.fr.entity.dto.CarrinhoDTO;
import com.fieldright.fr.entity.dto.PrecoDTO;
import com.fieldright.fr.entity.dto.ProductPriceDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.security.util.JwtUserUtil;
import com.fieldright.fr.service.interfaces.CompraService;
import com.fieldright.fr.util.enums.StatusVenda;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.validation.Valid;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/compras/v1")
public class CompraControllerImpl implements CompraController {

    private CompraService compraService;

    @Override
    @PostMapping(
            value = "/newCompra",
            params = "pais"
    )
    public Response<String> newCompra(@RequestBody CarrinhoDTO carrinho, @RequestParam String pais) throws IOException, JAXBException {
        return compraService.newCompra(carrinho, JwtUserUtil.getUserAuthenticated(), pais);

    }

    @Override
    @PostMapping(
            value = "/calculeFrete"
    )
    public Response<BigDecimal> calculeFrete(@RequestBody CarrinhoDTO carrinho) {
        return compraService.calculeFrete(carrinho);
    }

    @Override
    @GetMapping
    public Response<List<CarrinhoDTO>> findAll() {
        return compraService.findAll(JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PutMapping(
            value = "/evaluate"
    )
    public Response<HttpStatus> evaluate(@RequestBody Avaliacao avaliacao, long lojaId) {
        return compraService.evaluate(avaliacao, lojaId, JwtUserUtil.getUserAuthenticated());
    }
    
    @PostMapping(
            value = "/push",
            params = "mensagem"
    )
    public void sendPush(@RequestParam String mensagem) throws IOException, JAXBException {
         compraService.sendPush(JwtUserUtil.getUserAuthenticated(), mensagem);

    }


    @Override
    @PostMapping(
            value = "/fracaoPrice"
    )
    public Response<PrecoDTO> newPrice(@RequestBody @Valid ProductPriceDTO dto) {

        return compraService.newprice(dto);
    }
    @GetMapping("/pedido")
    @Override
    public Response getPedidoByUserAndStatus(@RequestParam(required = true, name = "userId") Long userIdLoja,
                                             @RequestParam(required = true, name = "status", defaultValue = "1") Long status,
                                             Pageable pageable){
       try
       {
           StatusVenda statusVenda =  StatusVenda.toEnum(status);

           return compraService.getPedidoByUserAndStatus(userIdLoja, statusVenda, pageable);

       }catch (Exception e){

           return new Response.Builder()
                   .withStatus(HttpStatus.BAD_REQUEST)
                   .withData(null)
                   .withErrors(Arrays.asList(e.getMessage()))
                   .build();
       }

    }
}
