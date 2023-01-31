package com.fieldright.fr.entity;

import com.fieldright.fr.util.enums.FormaPagamento;
import com.fieldright.fr.util.enums.StatusCompra;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private long compradorId;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Compra> compras;
    private String enderecoEntrega;
    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;
    private BigDecimal vlTotal;
    @Enumerated(EnumType.STRING)
    private StatusCompra statusCompra;
    private BigDecimal taxaEntrega;
    private String codigoPagamento;

    public static class Builder {
        private long compradorId;
        private List<Compra> compras;
        private String enderecoEntrega;
        private FormaPagamento formaPagamento;
        private BigDecimal vlTotal;
        private StatusCompra statusCompra;
        private BigDecimal taxaEntrega;
        private String codigoPagamento;

        public Builder withCompradorId(long compradorId) {
            this.compradorId = compradorId;
            return this;
        }

        public Builder withCompras(List<Compra> compras) {
            this.compras = compras;
            return this;
        }

        public Builder withEnderecoEntrega(String enderecoEntrega) {
            this.enderecoEntrega = enderecoEntrega;
            return this;
        }

        public Builder withFormaPagamento(FormaPagamento formaPagamento) {
            this.formaPagamento = formaPagamento;
            return this;
        }

        public Builder withVlTotal(BigDecimal vlTotal) {
            this.vlTotal = vlTotal;
            return this;
        }

        public Builder withStatus(StatusCompra statusCompra) {
            this.statusCompra = statusCompra;
            return this;
        }

        public Builder withTaxaEntrega(BigDecimal taxaEntrega) {
            this.taxaEntrega = taxaEntrega;
            return this;
        }

        public Builder withCodigoPagamento(String codigoPagamento) {
            this.codigoPagamento = codigoPagamento;
            return this;
        }

        public Carrinho build() {
            Carrinho carrinho = new Carrinho();

            carrinho.compradorId = this.compradorId;
            carrinho.compras = this.compras;
            carrinho.enderecoEntrega = this.enderecoEntrega;
            carrinho.formaPagamento = this.formaPagamento;
            carrinho.vlTotal = this.vlTotal;
            carrinho.statusCompra = this.statusCompra;
            carrinho.taxaEntrega = this.taxaEntrega;
            carrinho.setCodigoPagamento(codigoPagamento);

            return carrinho;
        }

    }
}
