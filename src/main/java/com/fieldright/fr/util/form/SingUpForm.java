package com.fieldright.fr.util.form;

import com.fieldright.fr.entity.CidadeAtuacao;
import com.fieldright.fr.entity.Endereco;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SingUpForm {

    @Email(message = "This email is not valid")
    @NotBlank(message = "The email must not be blank")
    private String email;
    @Size(min = 2)
    @NotBlank(message = "The first-name must not be blank")
    private String firstName;
    @Size(min = 2)
    @NotBlank(message = "The last-name must not be blank")
    private String lastName;
    @Pattern(regexp = "(d{2})d{5}-d{4}")
    private String phone;
    private String perfil;
    @Size(min = 3, max = 3, message = "O código ISO 3166-1 alfa-3 informado está incorreto")
    private String country;
    @CPF(message = "O CPF digitado não é válido")
    private String cpf;
    @CNPJ(message = "O CNPJ digitado não é válido")
    private String cnpj;
    @Size(min = 10, max = 10, message = "O NIF digitado não é válido")
    private String nif;
    @Size(min = 14, max = 14, message = "O bilhete digitado não é válido")
    private String bilheteIdentidade;
    @Size(min = 14, max = 14, message = "A carta de condução digitada não é válida")
    private String cartaConducao;
    @Size(min = 8, max = 11, message = "O RENAVAM digitado não é válido")
    private String renavam;
    @Size(min = 11, max = 11, message = "O CNH digitado não é válido")
    private String cnh;
    private Endereco endereco;
    private String tipoVeiculo;
    private CidadeAtuacao cidadeAtuacao;
    private Boolean possuiEntregadores;
    private Long categoria;

}
