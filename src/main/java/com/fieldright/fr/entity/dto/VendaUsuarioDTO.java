package com.fieldright.fr.entity.dto;

import com.fieldright.fr.entity.CidadeAtuacao;
import com.fieldright.fr.entity.Endereco;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendaUsuarioDTO {

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
    private Boolean possuiEntregadores;
    private CidadeAtuacao cidadeAtuacao;
    private Date proximaDesativacao;
    private String bilheteIdentidade;
}
