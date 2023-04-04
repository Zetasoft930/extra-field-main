package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Postagem;
import com.fieldright.fr.util.enums.StatusComentario;
import com.fieldright.fr.util.enums.StatusPostagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem,Long> {

    @Query(value = "   SELECT * FROM Postagem p  WHERE p.status = ?1 ORDER BY p.data DESC",nativeQuery = true)
    public Page<Postagem> findByStatus(String status, Pageable pageable);


    @Query(value = "select * from Postagem p\n" +
            "\tinner join Comentario c on c.postagem_id  = p.id\n" +
            "where p.status  = ? and c.status  = 'ACCEPTED' ORDER BY p.data DESC ",nativeQuery = true)
    public Page<Postagem> findByStatusAndComentarioApprove(String status, Pageable pageable);

}
