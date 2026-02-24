package com.bank.service;

import java.math.BigDecimal;

import com.bank.model.BankAccount;

public class AccountService {

    public BankAccount createAccount(String accountNumber, BigDecimal initialBalance) {
        return new BankAccount(accountNumber, initialBalance);
    }

    public void deposit(BankAccount account, BigDecimal amount) {
        account.deposit(amount);
    }

    public void withdraw(BankAccount account, BigDecimal amount) {
        account.withdraw(amount);
    }

    public BigDecimal getBalance(BankAccount account) {
        return account.getBalance();
    }

    public String getMiniStatement(BankAccount account) {
        return account.getMiniStatement();
    }
}
