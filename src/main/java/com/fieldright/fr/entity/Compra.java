package com.fieldright.fr.entity;

import com.fieldright.fr.util.enums.FormaPagamento;
import com.fieldright.fr.util.enums.StatusCompra;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private long productId;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private long vendedorId;
    private String vendedorName;
    private String compradorName;
    private String compradorPhone;
    @Size(min = 11, max = 11)
    private String vendedorPhone;
    private long compradorId;
    private BigDecimal vlPago;
    private int qtdComprada;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    @Enumerated(EnumType.STRING)
    private StatusCompra status;
    @OneToOne
    private Endereco enderecoLoja;
    @OneToOne
    private Endereco enderecoEntrega;
    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;
    private String distanciaEntrega;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> productPictures;
    private String unidadeMedida;
    private String observacao;
    private BigDecimal fracao;


    public static class Builder {

        private long productId;
        private long vendedorId;
        private int qtdComprada;
        private long compradorId;
        private BigDecimal vlPago;
        private String productName;
        private String vendedorName;
        private StatusCompra status;
        private String vendedorPhone;
        private String unidadeMedida;
        private String compradorName;
        private String compradorPhone;
        private Endereco enderecoLoja;
        private BigDecimal productPrice;
        private String distanciaEntrega;
        private Endereco enderecoEntrega;
        private String productDescription;
        private List<String> productPictures;
        private FormaPagamento formaPagamento;
        private String observacao;
        private BigDecimal fracao;

        public Builder withProductId(long productId) {
            this.productId = productId;
            return this;
        }

        public Builder withProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder withProductDescription(String productDescription) {
            this.productDescription = productDescription;
            return this;
        }

        public Builder withProductPrice(BigDecimal productPrice) {
            this.productPrice = productPrice;
            return this;
        }

        public Builder withVendedorId(long vendedorId) {
            this.vendedorId = vendedorId;
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

        public Builder withUnidadeMedida(String unidadeMedida) {
            this.unidadeMedida = unidadeMedida;
            return this;
        }

        public Builder withVlPago(BigDecimal vlPago) {
            this.vlPago = vlPago;
            return this;
        }

        public Builder withQtdComprada(int qtdComprada) {
            this.qtdComprada = qtdComprada;
            return this;
        }

        public Builder withEnderecoLoja(Endereco enderecoLoja) {
            this.enderecoLoja = enderecoLoja;
            return this;
        }

        public Builder withDistanciaEntrega(String distanciaEntrega) {
            this.distanciaEntrega = distanciaEntrega;
            return this;
        }

        public Builder withEnderecoEntrega(Endereco enderecoEntrega) {
            this.enderecoEntrega = enderecoEntrega;
            return this;
        }

        public Builder withStatus(StatusCompra status) {
            this.status = status;
            return this;
        }

        public Builder withFormaPagamento(FormaPagamento formaPagamento) {
            this.formaPagamento = formaPagamento;
            return this;
        }

        public Builder addProductPictures(List<String> productPictures) {
            this.productPictures = productPictures;
            return this;
        }
        
        public Builder withObservacao(String observacao) {
            this.observacao = observacao;
            return this;
        }

		public Builder withFracao(BigDecimal fracao) {
			this.fracao = fracao;
			return this;
		}

		public Compra build() {
            Compra compra = new Compra();

            compra.vlPago = this.vlPago;
            compra.status = this.status;
            compra.productId = this.productId;
            compra.vendedorId = this.vendedorId;
            compra.compradorId = this.compradorId;
            compra.qtdComprada = this.qtdComprada;
            compra.productName = this.productName;
            compra.vendedorName = this.vendedorName;
            compra.enderecoLoja = this.enderecoLoja;
            compra.productPrice = this.productPrice;
            compra.vendedorPhone = this.vendedorPhone;
            compra.unidadeMedida = this.unidadeMedida;
            compra.compradorName = this.compradorName;
            compra.compradorPhone = this.compradorPhone;
            compra.formaPagamento = this.formaPagamento;
            compra.enderecoEntrega = this.enderecoEntrega;
            compra.productPictures = this.productPictures;
            compra.distanciaEntrega = this.distanciaEntrega;
            compra.productDescription = this.productDescription;
            compra.observacao = this.observacao;
            compra.fracao = this.fracao;

            return compra;
        }

    }
}
