package com.daniel.iroribank.controller;

import com.daniel.iroribank.model.*;
import com.daniel.iroribank.service.AccountService;
import com.daniel.iroribank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
@CrossOrigin (origins = "*" , exposedHeaders = "Authorization")
public class AccountController {

    private AccountService accountService;

    private TransactionService transactionService;

    @PostMapping("/add/{id}")
    public Account createAccount(@PathVariable Long id) {
        return accountService.createAccount(id);
    }

    @PostMapping("/get/{accountNr}")
    public AccountDto getAccount(@PathVariable Long accountNr) {
        return accountService.getAccountWithTransactions(accountNr);
    }

    @PostMapping("/transfer")
    public List<Transaction> transfer(@RequestBody TransferDto transferDto) {
        return transactionService.transfer(transferDto);
    }

    @GetMapping("/balance")
    public Double getBalance(@RequestBody BalanceDto balanceDto){
        return accountService.getBalance(balanceDto);
    }

    @PostMapping("/deposit")
    public Transaction deposit(@RequestBody TransactionDto transactionDto) {
        return transactionService.deposit(transactionDto);
    }

    @PostMapping("/withdrawal")
    public Transaction withdrawal(@RequestBody TransactionDto transactionDto) {
        return transactionService.withdrawal(transactionDto);
    }

    @PostMapping("/getByUserId/{userId}")
    public List<Account> getByUserId(@PathVariable Long userId) {
        return accountService.getAccountsByUserId(userId);
    }
}
