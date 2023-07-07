package com.daniel.iroribank.service;

import com.daniel.iroribank.model.*;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@DirtiesContext
class AccountServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;

    @BeforeAll
    public void init() {
        User user = new User();
        user.setName("Daniel");
        user.setSsn("841026-5551");
        user.setPassword("123");
        user.setAccounts(new ArrayList<>());

        userRepository.save(user);
    }


    @Test
    void getAccountWithTransactions() {
        Account account = accountService.createAccount(1L);

        transactionService.deposit(new TransactionDto(123456L, 100.0));

        AccountDto accountDto = accountService.getAccountWithTransactions(123456L);
        assertEquals(100.0, accountDto.getTransactions().get(0).getAmount());
        assertEquals(TransactionType.DEPOSIT, accountDto.getTransactions().get(0).getTransactionType());

        transactionService.withdrawal(new TransactionDto(123456L, 100.0));
        accountDto = accountService.getAccountWithTransactions(123456L);
        assertEquals(2, accountDto.getTransactions().size());
    }

    @Test
    void createAccount() {

        Account account = accountService.createAccount(1L);

        assertEquals(123456L, account.getAccountNr());

        Account account2 = accountService.createAccount(1L);

        assertEquals(123457, account2.getAccountNr());

    }

    @Test
    void getAccountsByUserId() {
        Account account = accountService.createAccount(1L);
        Account account2 = accountService.createAccount(1L);

        List<Account> accounts = accountService.getAccountsByUserId(1L);

        assertEquals(123456L, accounts.get(0).getAccountNr());
        assertEquals(123457L, accounts.get(1).getAccountNr());
    }

    @Test
    void getBalance() {
        Account account = accountService.createAccount(1L);
        BalanceDto balanceDto = new BalanceDto(123456L);
        Double balance = accountService.getBalance(balanceDto);
        assertEquals(0.0, balance);
    }
}