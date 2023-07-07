package com.daniel.iroribank.service;

import com.daniel.iroribank.model.Account;
import com.daniel.iroribank.model.TransactionDto;
import com.daniel.iroribank.model.TransferDto;
import com.daniel.iroribank.model.User;
import com.daniel.iroribank.repo.AccountRepository;
import com.daniel.iroribank.repo.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@DirtiesContext
class TransactionServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;

    private Account account1;
    private Account account2;

    @BeforeAll
    public void init() throws InterruptedException {
        User user = new User();
        user.setName("Daniel");
        user.setSsn("841026-5551");
        user.setPassword("123");
        user.setAccounts(new ArrayList<>());

        userRepository.save(user);
    }

    @Test
    void deposit() {
        account1 = accountService.createAccount(1L);
        transactionService.deposit(new TransactionDto(123456L, 200.0));

        assertEquals(200.0,account1.getBalance());
    }

    @Test
    void withdrawal() {
        account1 = accountService.createAccount(1L);
        account2 = accountService.createAccount(1L);
        transactionService.deposit(new TransactionDto(123456L, 200.0));

        transactionService.withdrawal(new TransactionDto(123456L, 100.0));
        assertEquals(100.0, account1.getBalance());
    }

    @Test
    void transact() {
        account1 = accountService.createAccount(1L);
        account2 = accountService.createAccount(1L);
        transactionService.deposit(new TransactionDto(123456L, 200.0));

        TransferDto transferDto = new TransferDto(123456L, 123457L, 100.0);
        transactionService.transfer(transferDto);

        Account account3 = accountRepository.findAccountByAccountNr(123456L).get();
        Account account4 = accountRepository.findAccountByAccountNr(123457L).get();

        assertEquals(100.0, account3.getBalance());
        assertEquals(100.0, account4.getBalance());
    }

    @Test
    void transfer() {
    }

    @Test
    void isNegative() {
    }
}