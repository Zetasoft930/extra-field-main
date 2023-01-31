package com.fieldright.fr.repository;

import com.fieldright.fr.entity.pagSeguro.TransferInfos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferInfosRepository extends JpaRepository<TransferInfos, Long> {

    Optional<TransferInfos> findTransferInfosByTransferCode(String transferCode);
}
