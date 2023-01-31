package com.fieldright.fr.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Vendedor extends Usuario {

    private static final String PERFIL = "VENDEDOR";
    @NotBlank(message = "É obrigatório informar o código ISO 3166-1 alfa-3 para os vendedores!")
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
    @NotNull(message = "É preciso informar o endereço da loja")
    @OneToOne
    private Endereco endereco;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Product> products;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Venda> vendas;
    @OneToOne
    private Conta conta;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Avaliacao> avaliacoes;
    private Date proximaDesativacao;
    private Boolean possuiEntregadores;

    public String getCPFouCNPJ() {
        if (cpf == null || cpf.isBlank()) {
            return cnpj;
        } else {
            return cpf;
        }
    }

    public Boolean getPossuiEntregadores(){
        return possuiEntregadores == null ? false : possuiEntregadores;
    }
}
