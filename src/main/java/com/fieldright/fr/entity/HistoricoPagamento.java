package com.fieldright.fr.entity;

import com.fieldright.fr.util.enums.StatusHistoricoPagamento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class HistoricoPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey)
    private Conta conta;
    @Column(precision = 10,scale = 3)
    private BigDecimal valor;

    @Enumerated(value = EnumType.STRING)
    private StatusHistoricoPagamento status;

    @Column(unique = true)
    private String transferCode;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @CreationTimestamp
    @Column(updatable = true)
    private Timestamp updateAt;

}
