package com.fieldright.fr.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fieldright.fr.entity.Endereco;
import com.fieldright.fr.util.enums.StatusProduct;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {

    private long id;
    @NotBlank(message = "O nome do produto não pode ser vazio, nem nulo")
    private String name;
    private String country;
    @NotBlank(message = "É preciso informar a categoria do produto")
    private String category;
    private long vendedorId;
    private BigDecimal price;
    private String vendedorName;
    @Size(max = 1000)
    private String description;
    private List<String> pictures;
    private int tpPreparacaoDias;
    private int tpPreparacaoHoras;
    private Endereco enderecoLoja;
    private int tpPreparacaoMinutos;
    private BigDecimal quantityAvailable;
    @NotNull(message = "Obrigatório informar o peso do produto (kg)")
    private double peso;//kg
    @NotNull(message = "Obrigatório informar a altura do produto (cm)")
    private double altura;//cm
    @NotNull(message = "Obrigatório informar a largura do produto (cm)")
    private double largura;//cm
    @NotNull(message = "Obrigatório informar o comprimento do produto (cm)")
    private double comprimento;//cm
    private double pesoCubado;
    private double metrica;
    private String unidadeMedida;
    private Integer min_stock = 0;
    private StatusProduct status;
    private BigDecimal promotioPrice;
    private BigDecimal percentage;
}
