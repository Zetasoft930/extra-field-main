package com.fieldright.fr.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @NotNull
    private long usuarioId;
    private BigDecimal saldo;
    private String emailPagSeguro;
    private String chavePix;
    private String nome;
    private String instituicao;
    private Character tipoConta;
    private String agencia;
    private String nuConta;
    private Character digito;
    private String CPFouCNPJ;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    private Timestamp modifiedAt;

}
