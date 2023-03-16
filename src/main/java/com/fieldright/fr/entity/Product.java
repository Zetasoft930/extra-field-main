package com.fieldright.fr.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fieldright.fr.util.enums.StatusCompra;
import com.fieldright.fr.util.enums.StatusProduct;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private long vendedorId;
    private BigDecimal qtdReservada = BigDecimal.ZERO;
    private Integer tpPreparacaoDias;
    private Integer tpPreparacaoHoras;
    private Integer tpPreparacaoMinutos;
    @NotNull(message = "Obrigatório informar o peso do produto (kg)")
    private double peso;//kg
    @NotNull(message = "Obrigatório informar a altura do produto (cm)")
    private double altura;//cm
    @NotNull(message = "Obrigatório informar a largura do produto (cm)")
    private double largura;//cm
    @NotNull(message = "Obrigatório informar o comprimento do produto (cm)")
    private double comprimento;//cm
    private double pesoCubado;
    private double metrica;
    private String unidadeMedida;

    @NotBlank(message = "O nome do produto não pode ser vazio, nem nulo")
    private String name;
    @NotBlank(message = "É obrigatório informar a localização do produto")
    @Size(min = 3, max = 3, message = "O código ISO 3166-1 alfa-3 informado está incorreto")
    private String country;
    @NotBlank(message = "É preciso informar a categoria do produto")
    private String category;
    @Size(max = 1000)
    private String description;
    private String vendedorName;

    private BigDecimal price = new BigDecimal(0);
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    @OneToOne
    private Endereco enderecoLoja;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> pictures;
    private BigDecimal quantityAvailable = BigDecimal.ZERO;
    private Integer min_stock = 0;
    @Enumerated(EnumType.STRING)
    private StatusProduct status;



    public BigDecimal qtdDisponivelParaCompra() {

        return quantityAvailable.subtract(qtdReservada);
    }

    public boolean isValid(){


        return peso >= 0 &&  altura >= 0 && altura >= 0 && !name.trim().isEmpty()
                && !country.trim().isEmpty() && !category.isEmpty() && !unidadeMedida.trim().isEmpty();
    }
}
