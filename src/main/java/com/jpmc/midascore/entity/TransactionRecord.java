package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import org.h2.engine.User;

@Entity
public class TransactionRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long senderId;
    private long recipientId;
    private float amount;

    protected TransactionRecord() {}

    public TransactionRecord(long senderId, long recipientId, float amount) {
        this.senderId = senderId;
        this.amount = amount;
        this.recipientId = recipientId;
    }

    public long getId() {
        return id;
    }
    public long getSenderId() {
        return senderId;
    }
    public long getRecipientId() {
        return recipientId;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }
    public void setRecipient(long recipientId) {
        this.recipientId = recipientId;
    }
}
