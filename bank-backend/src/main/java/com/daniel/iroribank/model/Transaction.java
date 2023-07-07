package com.daniel.iroribank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Double amount;
    private TransactionType transactionType;
    private Timestamp timestamp;
    @ManyToOne
    @JoinColumn
    private Account account;
}
