package com.fieldright.fr.entity.emis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class TransactionGPO {

    public static final String ACCEPTED = "ACCEPTED";
    public static final String REJECTED = "REJECTED";

    private BigDecimal amount;
    private String status;
    private Reference reference;

    public TransactionGPO(String referenceId) {
        Reference reference = new Reference(referenceId);
        this.reference = reference;
    }

    public String getReferenceId() {
        return this.reference.id;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    private class Reference {
        String orderOrigin;
        String id;

        Reference(String id) {
            this.id = id;
        }
    }
}
