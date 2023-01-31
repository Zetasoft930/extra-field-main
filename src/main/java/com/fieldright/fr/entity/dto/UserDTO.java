package com.fieldright.fr.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fieldright.fr.entity.Avaliacao;
import com.fieldright.fr.entity.CidadeAtuacao;
import com.fieldright.fr.entity.Endereco;
import com.fieldright.fr.util.enums.TipoVeiculo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private long id;
    private String nif;
    private String cnh;
    private String cpf;
    private String cnpj;
    private String email;
    private String phone;
    private String avatar;
    private String perfil;
    private String country;
    private String renavam;
    private ContaDTO conta;
    private boolean active;
    private String lastName;
    private String password;
    private String firstName;
    private Endereco endereco;
    private Timestamp createdAt;
    private List<VendaDTO> vendas;
    private boolean alterPassword;
    private TipoVeiculo tipoVeiculo;
    private Date proximaDesativacao;
    private String bilheteIdentidade;
    private List<CarrinhoDTO> compras;
    private List<ProductDTO> products;
    private List<Avaliacao> avaliacoes;
    private Boolean possuiEntregadores;
    private CidadeAtuacao cidadeAtuacao;
    private List<String> enderecosSalvos;


}
