package com.bank;

import com.bank.model.BankAccount;
import com.bank.exception.InsufficientFundsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    private BankAccount account;

    @BeforeEach
    void setUp() {
        account = new BankAccount("12345", 1000);
    }

    @Test
    void testDeposit() {
        account.deposit(500);
        assertEquals(1500, account.getBalance());
    }

    @Test
    void testWithdraw() {
        account.withdraw(300);
        assertEquals(700, account.getBalance());
    }

    @Test
    void testWithdrawInsufficientFunds() {
        assertThrows(InsufficientFundsException.class, () -> {
            account.withdraw(2000);
        });
    }

    @Test
    void testDepositNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(-100);
        });
    }

    @Test
    void testTransactionHistorySize() {
        account.deposit(200);
        account.withdraw(100);
        assertEquals(2, account.getTransactions().size());
    }
}