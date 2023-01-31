package com.fieldright.fr.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fieldright.fr.util.enums.StatusDepoimento;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Depoimento {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String comentario;
    private long usuarioId;
    @Enumerated(EnumType.STRING)
    private StatusDepoimento status;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

}
