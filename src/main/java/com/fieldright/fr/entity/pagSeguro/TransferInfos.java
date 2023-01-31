package com.fieldright.fr.entity.pagSeguro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferInfos {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String authorizationCode;
    private String transferCode;
    private String transactionCode;
    private String receiverEmail;
    private double amount;
    private String description;
    private boolean error;

}
