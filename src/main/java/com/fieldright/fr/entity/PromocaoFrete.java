package com.fieldright.fr.entity;

import java.math.BigDecimal;
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
import lombok.experimental.SuperBuilder;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@SuperBuilder(toBuilder = true)
public class PromocaoFrete {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp createdAt;
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp startDate;
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp endDate;
	private BigDecimal percentage;
	private long lojaId;
	private boolean hide;

}
