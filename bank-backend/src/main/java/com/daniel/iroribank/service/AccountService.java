package com.daniel.iroribank.service;

import com.daniel.iroribank.model.*;
import com.daniel.iroribank.repo.AccountRepository;
import com.daniel.iroribank.repo.TransactionRepository;
import com.daniel.iroribank.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public AccountDto getAccountWithTransactions(Long accountNr) {
        Account account = accountRepository.findAccountByAccountNr(accountNr).orElse(null);
        if (account != null) {
            List<Transaction> transactions = transactionRepository.findByAccount(account);
            transactions = transactions.stream().sorted(Comparator.comparing(Transaction::getTimestamp).reversed()).toList();
            return new AccountDto(account, transactions);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such account nr");
        }
    }

    public Account createAccount(Long id) {
        Long maxAccountNr = accountRepository.getMaxTransactionId();

        final Long accountNr = maxAccountNr == null ? 123456L : maxAccountNr + 1;

        Account account = new Account();
        account.setAccountNr(accountNr);
        account.setBalance(0.0);

        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        } else {
            List<Account> accounts = user.getAccounts();
            accounts.add(account);
            user.setAccounts(accounts);
        }

        User updatedUser = userRepository.save(user);
        List<Account> updatedAccounts = updatedUser.getAccounts().stream().filter(a -> a.getAccountNr().equals(accountNr)).toList();
        if (updatedAccounts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Account not created");
        } else {
            return updatedAccounts.get(0);
        }
    }

    public List<Account> getAccountsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getAccounts();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public Double getBalance(BalanceDto balanceDto) {
        Optional<Account> accountOptional = accountRepository.findAccountByAccountNr(balanceDto.getAccountNr());
        if (accountOptional.isPresent()) {
            return accountOptional.get().getBalance();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not exist");
        }
    }
}
