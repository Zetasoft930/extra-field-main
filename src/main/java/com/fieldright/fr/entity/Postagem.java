package com.fieldright.fr.entity;


import com.fieldright.fr.util.enums.StatusPostagem;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Data
@Entity
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String titulo;
    private String descricao;

    private String imagem;
    private LocalDateTime data = LocalDateTime.now();

    @Enumerated(value = EnumType.STRING)
    private StatusPostagem status = StatusPostagem.ACTIVADO;

}
