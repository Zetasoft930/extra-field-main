package com.fieldright.fr.util.form;

import com.fieldright.fr.entity.CidadeAtuacao;
import com.fieldright.fr.entity.Endereco;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateForm {

    @Email(message = "This email is not valid")
    private String email;
    private String password;
    @Size(min = 2)
    private String firstName;
    @Size(min = 2)
    private String lastName;
    @Pattern(regexp = "(d{2})d{5}-d{4}")
    private String phone;
    @CPF(message = "O CPF digitado não é válido")
    private String cpf;
    @CNPJ(message = "O CNPJ digitado não é válido")
    private String cnpj;
    @Size(min = 11, max = 11)
    private String renavam;
    @Size(min = 11, max = 11)
    private String cnh;
    private Endereco endereco;
    private CidadeAtuacao cidadeAtuacao;
    private Boolean possuiEntregadores;
    private String tipoVeiculo;
}
