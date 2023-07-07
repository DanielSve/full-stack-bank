package com.daniel.iroribank.service;

import com.daniel.iroribank.model.*;
import com.daniel.iroribank.repo.AccountRepository;
import com.daniel.iroribank.repo.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public Transaction deposit(TransactionDto transactionDto) {
        return transact(TransactionType.DEPOSIT, transactionDto);
    }

    public Transaction withdrawal(TransactionDto transactionDto) {
        return transact(TransactionType.WITHDRAWAL, transactionDto);
    }


    @Transactional
    public Transaction transact(TransactionType transactionType, TransactionDto transactionDto) {
        Optional<Account> accountOptional = accountRepository.findAccountByAccountNr(transactionDto.getAccountNr());
        Account account;
        if(accountOptional.isPresent()) {
            account = accountOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not exist");
        }
        if(isNegative(transactionDto.getAmount())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Negative number");
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setAmount(transactionType.equals(TransactionType.WITHDRAWAL) ? transactionDto.getAmount() * -1 : transactionDto.getAmount());
        transaction.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        transaction.setAccount(account);

        Double balanceIfTransact = account.getBalance() + transaction.getAmount();
        if(transactionType.equals(TransactionType.WITHDRAWAL) && balanceIfTransact < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough money");
        }
        transaction.getAccount().setBalance(balanceIfTransact);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public List<Transaction> transfer(TransferDto transferDto) {
       Account fromAcc = accountRepository.findAccountByAccountNr(transferDto.getFromAccount()).orElse(null);
       Account toAcc = accountRepository.findAccountByAccountNr(transferDto.getToAccount()).orElse(null);

       if(isNegative(transferDto.getAmount())) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Negative number");
       }
       if(fromAcc != null && toAcc != null) {
           Transaction transactionFrom = new Transaction();
           transactionFrom.setTransactionType(TransactionType.TRANSFER);
           transactionFrom.setAmount(transferDto.getAmount() * -1);
           transactionFrom.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
           transactionFrom.setAccount(fromAcc);

           Double balanceOnFromAccIfTransfer = fromAcc.getBalance() - transferDto.getAmount();
           if(balanceOnFromAccIfTransfer < 0) {
               System.out.println("Not enough money");
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough money");
           }

           Transaction transactionTo = new Transaction();
           transactionTo.setTransactionType(TransactionType.TRANSFER);
           transactionTo.setAmount(transferDto.getAmount());
           transactionTo.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
           transactionTo.setAccount(toAcc);

           transactionFrom.getAccount().setBalance(balanceOnFromAccIfTransfer);
           transactionTo.getAccount().setBalance(transactionTo.getAccount().getBalance() + transferDto.getAmount());


           List<Transaction> transactions = new ArrayList<>();
           transactions.add(transactionRepository.save(transactionFrom));
           transactions.add(transactionRepository.save(transactionTo));
           return transactions;
       } else {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or both accounts are invalid");
       }
    }

    public static boolean isNegative(double d) {
        return Double.compare(d, 0.0) < 0;
    }
}
