package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Comentario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

	@Query(value = "select u.first_name, u.last_name, u.avatar , u.dtype, d.created_at, d.comentario, d.status  \r\n"
			+ "from comentario d join usuario u on d.usuario_id = u.id\r\n"
			+ "where (u.id = ?1 or ?1 is null) and (d.status like ?2 or ?2 is null) and (d.created_at between ?3 and ?4)", nativeQuery = true)
	Page<Object[]> findByFilters(long usuarioId, String status, LocalDate startdate,
			LocalDate endDate, Pageable pageable);

}
