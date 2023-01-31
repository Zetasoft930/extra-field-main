package com.fieldright.fr.entity.architecture;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fieldright.fr.entity.SuperCategory;

import java.util.List;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String name;
    @ManyToOne
    private SuperCategory superCategory;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> pictures;

    public Category(String name) {
        this.name = name;
    }
}
