package com.fieldright.fr.entity;

import com.fieldright.fr.util.enums.FormaPagamento;
import com.fieldright.fr.util.enums.StatusVenda;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private long carrinhoId;
    private long vendedorId;
    private long compradorId;
    private String vendedorName;
    private String compradorName;
    private String vendedorPhone;
    private String compradorPhone;
    @OneToOne
    private Endereco enderecoEntrega;
    private BigDecimal vlTotal;
    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Compra> compras;
    @Enumerated(EnumType.STRING)
    private StatusVenda status;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    private Timestamp confirmadaAt;
    private Timestamp rejeitadaAt;
    private Timestamp aCaminhoAt;
    private Timestamp finalizadaAt;
    private BigDecimal taxaFieldrightVenda;
    private BigDecimal taxaFieldrightEntrega;
    private BigDecimal valorLiquido;

    public static class Builder {
        private long carrinhoId;
        private long vendedorId;
        private long compradorId;
        private BigDecimal vlTotal;
        private StatusVenda status;
        private String vendedorName;
        private String compradorName;
        private String vendedorPhone;
        private String compradorPhone;
        private Endereco enderecoEntrega;
        private FormaPagamento formaPagamento;

        public Builder withCarrinhoId(long carrinhoId) {
            this.carrinhoId = carrinhoId;
            return this;
        }

        public Builder withVendedorId(long vendedorId) {
            this.vendedorId = vendedorId;
            return this;
        }

        public Builder withCompradorId(long compradorId) {
            this.compradorId = compradorId;
            return this;
        }

        public Builder withCompradorName(String compradorName) {
            this.compradorName = compradorName;
            return this;
        }

        public Builder withCompradorPhone(String compradorPhone) {
            this.compradorPhone = compradorPhone;
            return this;
        }

        public Builder withVendedorName(String vendedorName) {
            this.vendedorName = vendedorName;
            return this;
        }

        public Builder withVendedorPhone(String vendedorPhone) {
            this.vendedorPhone = vendedorPhone;
            return this;
        }

        public Builder withEnderecoEntrega(Endereco enderecoEntrega) {
            this.enderecoEntrega = enderecoEntrega;
            return this;
        }

        public Builder withVlTotal(BigDecimal vlTotal) {
            this.vlTotal = vlTotal;
            return this;
        }

        public Builder withFormaPagamento(FormaPagamento formaPagamento) {
            this.formaPagamento = formaPagamento;
            return this;
        }

        public Builder withStatus(StatusVenda status) {
            this.status = status;
            return this;
        }

        public Venda build() {
            Venda venda = new Venda();
            venda.status = this.status;
            venda.vlTotal = this.vlTotal;
            venda.compras = new ArrayList<>();
            venda.carrinhoId = this.carrinhoId;
            venda.vendedorId = this.vendedorId;
            venda.compradorId = this.compradorId;
            venda.vendedorName = this.vendedorName;
            venda.compradorName = this.compradorName;
            venda.vendedorPhone = this.vendedorPhone;
            venda.formaPagamento = this.formaPagamento;
            venda.compradorPhone = this.compradorPhone;
            venda.enderecoEntrega = this.enderecoEntrega;

            return venda;
        }
    }
}
