package com.fieldright.fr.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fieldright.fr.entity.Endereco;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompraDTO {

    private long id;
    private String status;
    private long productId;
    private Integer qtdComprada;
    private long vendedorId;
    private long compradorId;
    private BigDecimal vlPago;
    private String productName;
    private String vendedorName;
    private Timestamp createdAt;
    private String vendedorPhone;
    private String compradorName;
    private String unidadeMedida;
    private String compradorPhone;
    private String formaPagamento;
    private Endereco enderecoLoja;
    private BigDecimal taxaEntrega;
    private BigDecimal productPrice;
    private String distanciaEntrega;
    private Endereco enderecoEntrega;
    private String productDescription;
    private List<String> productPictures;
    private String observacao;
    private BigDecimal fracao;
}
