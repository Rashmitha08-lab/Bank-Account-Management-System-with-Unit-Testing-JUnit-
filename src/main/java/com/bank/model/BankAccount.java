package com.bank.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.bank.exception.InsufficientFundsException;

public class BankAccount {

    private final String accountNumber;
    private BigDecimal balance;
    private final List<Transaction> transactions;

    public BankAccount(String accountNumber, BigDecimal initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance == null ? BigDecimal.ZERO : initialBalance;
        this.transactions = new ArrayList<>();
    }

    public BankAccount(String accountNumber, double initialBalance) {
        this(accountNumber, BigDecimal.valueOf(initialBalance));
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public synchronized void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit must be positive");
        }
        balance = balance.add(amount);
        transactions.add(new Transaction(Transaction.Type.DEPOSIT, amount));
    }

    public synchronized void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal must be positive");
        }
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Not enough balance");
        }
        balance = balance.subtract(amount);
        transactions.add(new Transaction(Transaction.Type.WITHDRAW, amount));
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

    public synchronized List<Transaction> getTransactions() {
        return Collections.unmodifiableList(new ArrayList<>(transactions));
    }

    public synchronized String getMiniStatement() {
        StringBuilder sb = new StringBuilder();
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        sb.append("Mini Statement for Account: ").append(accountNumber).append("\n");
        for (Transaction t : transactions) {
            sb.append(t.getTimestamp().format(df))
              .append("  ")
              .append(t.getType())
              .append("  ")
              .append(nf.format(t.getAmount()))
              .append("\n");
        }
        sb.append("Current Balance: ").append(nf.format(balance));
        return sb.toString();
    }
}
