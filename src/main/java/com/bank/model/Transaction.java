package com.bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

    public enum Type { DEPOSIT, WITHDRAW }

    private final String transactionId;
    private final Type type;
    private final BigDecimal amount;
    private final LocalDateTime timestamp;

    public Transaction(Type type, BigDecimal amount) {
        this.transactionId = UUID.randomUUID().toString();
        this.type = Objects.requireNonNull(type, "type");
        this.amount = amount == null ? BigDecimal.ZERO : amount;
        this.timestamp = LocalDateTime.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Type getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s", transactionId, type, amount);
    }
}
