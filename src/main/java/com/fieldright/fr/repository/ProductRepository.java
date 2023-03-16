package com.fieldright.fr.repository;

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
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Query("select p " +
                    "   from Product as p " +
                    "       join Usuario as u " +
                    "           on      p.vendedorId = u.id " +
                    "               and u.active = true "
    )
    List<Product> findAll();

    @Query("select p " +
                    "   from Product as p " +
                    "       join Usuario as u " +
                    "           on" +
                    "               p.vendedorId = u.id " +
                    "           and u.active = true " +
                    "           where " +
                    "               u.id = ?1 " +
                    "           and LOWER(p.name) like %?2% " +
                    "           and LOWER(p.category) like %?3% and (p.quantityAvailable-p.qtdReservada) > 0"
    )
    Page<Product> findByFilters(long vendedorId, String name, String category, Pageable pageable);

    @Query("select p " +
                    "   from Product as p " +
                    "       join Usuario as u " +
                    "           on" +
                    "               p.vendedorId = u.id " +
                    "           and u.active = true " +
                    "           where " +
                    "               LOWER(p.name) like %?1% " +
                    "           and LOWER(p.category) like %?2%"
    )
    Page<Product> findByFilters(String name, String category, Pageable pageable);
    
    @Query("select count(1) " +
            "   from Product as p " +
            "       join Usuario as u " +
            "           on" +
            "               p.vendedorId = u.id " +
            "           and u.active = true " +
            "           where " +
            "               LOWER(p.name) like ?1 " +
            "           and LOWER(p.unidadeMedida) like ?2"
)
int findByNameUserAndUnidade(String name, String unidade, long user);
    
@Query(value = "select * from product p where \r\n"
		+ "(p.vendedor_id =?1 or ?1 = 0) \r\n"
		+ "and ((date(p.created_at) between ?2 \r\n"
		+ "and current_date) or ?2 = current_date)\r\n"
		+ "and (p.category like ?3 or  ?3 is null) \r\n"
		+ "and p.\"name\" like %?4% \r\n"
		+ "and (p.price < ?5 or ?5 = 0) and status like %?6% ", nativeQuery = true)
Page<Product> findByFilters(long loja, LocalDate searchDate, String category, String name, BigDecimal price, String status, Pageable pageable);

@Query(value = "select p.* from product p inner join compra c \r\n"
		+ "on  p.id = c.product_id where \r\n"
		+ "(c.vendedor_id =?1 or ?1 is null) \r\n"
		+ "and ((date(p.created_at) between ?2 \r\n"
		+ "and current_date) or ?2 = current_date)\r\n"
		+ "and (p.category like ?3 or ?3 is null)\r\n"
		+ "and p.\"name\" like %?4% \r\n"
		+ "and (p.price < ?5 or ?5 = 0) and p.status like %?6% \r\n"
		+ "group by c.product_id, p.id\r\n"
		+ "order by count(c.qtd_comprada) desc", nativeQuery = true)
Page<Product> findMaisVendidosFilters(long loja, LocalDate searchDate, String category, String name, BigDecimal price, String status, Pageable pageable);

@Query(value = "select p.* from promocao_product pp join product p on pp.product_id = p.id  \r\n"
		+ "where current_date between pp.start_date and pp.end_date and (pp.product_id = ?1 or ?1 = 0)", nativeQuery = true)
Page<Product> findPromotionProducts(long productId, Pageable pageable);

@Query(value = "select p.* from product p \n" +
		"where p.min_stock >= (p.quantity_available - p.qtd_reservada)\n" +
		"and p.status ='CONFIRMED'\n",nativeQuery = true)
    List<Product> findStockEmBaixoByUserLoja();
}
