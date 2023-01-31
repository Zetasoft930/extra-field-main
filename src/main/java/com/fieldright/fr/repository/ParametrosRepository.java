package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Parametros;
import com.fieldright.fr.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ParametrosRepository extends PagingAndSortingRepository<Parametros, Long> {
    
@Query(value = "select value from parametros where parameter_key like ?1", nativeQuery = true)
String findValueByKey(String key);
}
