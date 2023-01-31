package com.fieldright.fr.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class ProductFracao {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
	private int limiteFracao;
	private long productId;
	@CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

}
