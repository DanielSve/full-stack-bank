package com.daniel.iroribank.repo;

import com.daniel.iroribank.model.Account;
import com.daniel.iroribank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount(Account account);
}
