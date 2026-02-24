package com.bank;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bank.exception.InsufficientFundsException;
import com.bank.model.BankAccount;
import com.bank.model.Transaction;

public class BankAccountTest {

    private BankAccount account;

    @BeforeEach
    void setUp() {
        account = new BankAccount("T-100", BigDecimal.valueOf(1000));
    }

    @Test
    void testDepositSuccess() {
        account.deposit(BigDecimal.valueOf(500));
        assertEquals(0, BigDecimal.valueOf(1500).compareTo(account.getBalance()));
    }

    @Test
    void testWithdrawSuccess() {
        account.withdraw(BigDecimal.valueOf(300));
        assertEquals(0, BigDecimal.valueOf(700).compareTo(account.getBalance()));
    }

    @Test
    void testWithdrawExceedingBalance() {
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(BigDecimal.valueOf(2000)));
    }

    @Test
    void testTransactionHistorySizeIncrease() {
        account.deposit(BigDecimal.valueOf(200));
        account.withdraw(BigDecimal.valueOf(100));
        List<Transaction> txs = account.getTransactions();
        assertEquals(2, txs.size());
        assertEquals(Transaction.Type.DEPOSIT, txs.get(0).getType());
        assertEquals(Transaction.Type.WITHDRAW, txs.get(1).getType());
    }

    @Test
    void testConcurrencySafety() throws InterruptedException {
        int depositThreads = 10;
        int withdrawThreads = 10;
        CountDownLatch ready = new CountDownLatch(depositThreads + withdrawThreads);
        CountDownLatch start = new CountDownLatch(1);
        Thread[] threads = new Thread[depositThreads + withdrawThreads];

        for (int i = 0; i < depositThreads; i++) {
            threads[i] = new Thread(() -> {
                ready.countDown();
                try { start.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                account.deposit(BigDecimal.valueOf(100));
            });
        }

        for (int i = 0; i < withdrawThreads; i++) {
            threads[depositThreads + i] = new Thread(() -> {
                ready.countDown();
                try { start.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                account.withdraw(BigDecimal.valueOf(50));
            });
        }

        for (Thread t : threads) t.start();
        ready.await();
        start.countDown();
        for (Thread t : threads) t.join();

        // Expected: +10*100 -10*50 = +500
        assertEquals(0, BigDecimal.valueOf(1500).compareTo(account.getBalance()));
        assertEquals(depositThreads + withdrawThreads, account.getTransactions().size());
    }
}
