package com.fieldright.fr.entity;

import com.fieldright.fr.util.enums.TipoVeiculo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Motorista extends Usuario {

    private static final String PERFIL = "MOTORISTA";
    @CPF(message = "O CPF digitado não é válido")
    private String cpf;
    @Size(min = 14, max = 14, message = "O bilhete digitado não é válido")
    private String bilheteIdentidade;
    @Size(min = 8, max = 11, message = "O RENAVAM digitado não é válido")
    private String renavam;
    @Size(min = 11, max = 11, message = "O CNH digitado não é válido")
    private String cnh;
    @Size(min = 14, max = 14, message = "A carta de condução digitada não é válida")
    private String cartaConducao;
    @OneToOne
    private Conta conta;
    private Long cidadeAtuacao;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "É obrigatório informar o tipo de veículo que será usado para entregas")
    private TipoVeiculo tipoVeiculo;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Entrega> entregasRejeitadas;

    public Set<Entrega> getEntregasRejeitadas() {
        if (entregasRejeitadas == null) {
            entregasRejeitadas = new HashSet<>();
        }

        return entregasRejeitadas;
    }
}
