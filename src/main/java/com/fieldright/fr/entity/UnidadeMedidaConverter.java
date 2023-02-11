package com.fieldright.fr.entity;


import com.fieldright.fr.entity.architecture.UnidadeMedida;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class UnidadeMedidaConverter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn( foreignKey= @ForeignKey)
    private UnidadeMedida unidadeMedida_origem;
    @ManyToOne
    @JoinColumn( foreignKey= @ForeignKey)
    private UnidadeMedida unidadeMedida_destino;

    @Column(precision = 10,scale = 4)
    private BigDecimal equivale;

}
