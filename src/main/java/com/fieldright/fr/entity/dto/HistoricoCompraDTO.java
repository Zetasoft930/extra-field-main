package com.fieldright.fr.entity.dto;

import com.fieldright.fr.entity.Endereco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoCompraDTO {

    private String status;
    private BigDecimal montante = BigDecimal.ZERO;
    private String data;
}
