package com.fieldright.fr.entity;


import com.fieldright.fr.util.enums.StatusPostagem;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String titulo;
    private String descricao;

    private String imagem;
    private LocalDateTime data = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey)
    private CategoriaPostagem categoria;

    @Enumerated(value = EnumType.STRING)
    private StatusPostagem status = StatusPostagem.ACTIVADO;


    @OneToMany(mappedBy = "postagem",fetch = FetchType.LAZY)
    private Set<Comentario> comentarios = new HashSet<>();

}
