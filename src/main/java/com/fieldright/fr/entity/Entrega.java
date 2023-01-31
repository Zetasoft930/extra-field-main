package com.fieldright.fr.entity;

import com.fieldright.fr.util.enums.StatusEntrega;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private long vendaId;
    private long entregadorId;
    private String vendedorName;
    private String compradorName;
    private String entregadorName;
    private String vendedorPhone;
    private String compradorPhone;
    private String entregadorPhone;
    private Timestamp aceitaAt;
    @CreationTimestamp
    private Timestamp createdAt;
    private Timestamp aCaminhoAt;
    private Timestamp entregueAt;
    @OneToOne
    private Endereco enderecoLoja;
    @OneToOne
    private Endereco enderecoEntrega;
    private String distanciaEntrega;
    @Enumerated(EnumType.STRING)
    private StatusEntrega statusEntrega;
    private BigDecimal valorRemuneracao;
    @ElementCollection
    private Map<String, BigDecimal> valoresRemuneracao;
    private double pesoCubado;

    public static class Builder {

        private long vendaId;
        private String vendedorName;
        private String compradorName;
        private String vendedorPhone;
        private String compradorPhone;
        private double pesoCubado;
        private Endereco enderecoLoja;
        private String distanciaEntrega;
        private Endereco enderecoEntrega;
        private StatusEntrega statusEntrega;
        private Map<String, BigDecimal> valoresRemuneracao;

        public Builder withVendaId(long vendaId) {
            this.vendaId = vendaId;
            return this;
        }

        public Builder withPesoCubado(double pesoCubado) {
            this.pesoCubado = pesoCubado;
            return this;
        }

        public Builder withEnderecoLoja(Endereco enderecoLoja) {
            this.enderecoLoja = enderecoLoja;
            return this;
        }

        public Builder withEnderecoEntrega(Endereco enderecoEntrega) {
            this.enderecoEntrega = enderecoEntrega;
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

        public Builder withCompradorName(String compradorName) {
            this.compradorName = compradorName;
            return this;
        }

        public Builder withCompradorPhone(String compradorPhone) {
            this.compradorPhone = compradorPhone;
            return this;
        }

        public Builder withDistanciaEntrega(String distanciaEntrega) {
            this.distanciaEntrega = distanciaEntrega;
            return this;
        }

        public Builder withValoresRemuneracao(Map<String, BigDecimal> valoresRemuneracao) {
            this.valoresRemuneracao = valoresRemuneracao;
            return this;
        }

        public Builder withStatus(StatusEntrega statusEntrega) {
            this.statusEntrega = statusEntrega;
            return this;
        }

        public Entrega build() {
            Entrega entrega = new Entrega();
            entrega.vendaId = this.vendaId;
            entrega.pesoCubado = this.pesoCubado;
            entrega.enderecoLoja = this.enderecoLoja;
            entrega.vendedorName = this.vendedorName;
            entrega.compradorName = this.compradorName;
            entrega.vendedorPhone = this.vendedorPhone;
            entrega.statusEntrega = this.statusEntrega;
            entrega.compradorPhone = this.compradorPhone;
            entrega.enderecoEntrega = this.enderecoEntrega;
            entrega.distanciaEntrega = this.distanciaEntrega;
            entrega.valoresRemuneracao = this.valoresRemuneracao;

            return entrega;
        }

    }
}
