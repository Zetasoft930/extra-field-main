package com.fieldright.fr.entity;

import com.fieldright.fr.util.enums.StatusComentario;
import com.fieldright.fr.util.enums.StatusDepoimento;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class Comentario {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String comentario;
    private long usuarioId;
    @Enumerated(EnumType.STRING)
    private StatusComentario status =StatusComentario.PENDING;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey)
    private Postagem postagem;


}
