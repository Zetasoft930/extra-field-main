package com.fieldright.fr.entity;

import com.fieldright.fr.util.enums.StatusCategoriPostagem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaPostagem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String name;
    private LocalDateTime data  = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private StatusCategoriPostagem status = StatusCategoriPostagem.ACTIVADO;


    @OneToMany(mappedBy = "categoria")
    private Set<Postagem> postagems = new HashSet<>();
}
