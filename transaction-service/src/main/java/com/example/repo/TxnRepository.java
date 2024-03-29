package com.example.repo;

import com.example.model.Transaction;
import com.example.model.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TxnRepository extends JpaRepository<Transaction,Integer> {
    Transaction findByExternalTxnId(String externalTxnId);
    @Modifying
    @Query("update Transaction t set t.transactionStatus= ?2 where t.externalTxnId = ?1")
    void updateTxnStatus(String externalTxnId, TransactionStatus transactionStatus);
}
