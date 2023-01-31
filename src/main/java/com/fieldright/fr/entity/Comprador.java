package com.fieldright.fr.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Comprador extends Usuario {

    private static final String PERFIL = "COMPRADOR";
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Carrinho> compras;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Endereco> enderecosSalvos;
}
