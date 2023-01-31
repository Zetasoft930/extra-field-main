package com.fieldright.fr.entity.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class PromocaoFreteDTO {
	
	private long id;
	private Timestamp createdAt;
	private Timestamp startDate;
	private Timestamp endDate;
	private BigDecimal percentage;
	private long lojaId;
	private boolean hide;

}
