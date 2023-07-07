package com.daniel.iroribank.repo;

import com.daniel.iroribank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT max(accountNr) FROM Account")
    Long getMaxTransactionId();

    Optional<Account> findAccountByAccountNr(Long AccountNr);
}
