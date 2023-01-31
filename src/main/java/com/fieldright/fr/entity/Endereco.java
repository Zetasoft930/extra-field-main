package com.fieldright.fr.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @NotBlank(message = "É preciso informar o país")
    private String pais;
    private String estado;
    @NotBlank(message = "É preciso informar a cidade")
    private String cidade;
    @NotBlank(message = "É preciso informar o bairro")
    private String bairro;
    @NotBlank(message = "É preciso informar a rua")
    private String rua;
    @NotBlank(message = "É preciso informar o número")
    private String numero;
    @NotBlank(message = "É preciso informar o CEP")
    @Pattern(
                    regexp = "^(([0-9]{2}\\.[0-9]{3}-[0-9]{3})|([0-9]{2}[0-9]{3}-[0-9]{3})|([0-9]{8}))$",
                    message = "O CEP entrado é inválido"
    )
    private String cep;
    private String complemento;

    @Override
    public String toString() {
        return rua + ", " + numero + ", " + complemento + " - " + bairro + ", " + cidade + " - " + estado + ", " + cep + ", " + pais;
    }
}
