package com.fieldright.fr.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private byte estrelas;
    private String comentario;
    private long avaliadorId;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
}
