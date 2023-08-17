package com.habapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.habapp.models.converters.DateConverter;

import java.util.Date;

@Entity(tableName = "sack")
@TypeConverters(DateConverter.class)
public class Sack {
    @PrimaryKey(autoGenerate = true)
    private long sackId;
    private long farmId;

    public Sack(@NonNull Date creationDate, @NonNull Date expirationDate, int amount, @NonNull String description) {
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.amount = amount;
        this.description = description;
    }

    @NonNull
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(@NonNull Date creationDate) {
        this.creationDate = creationDate;
    }

    @NonNull
    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(@NonNull Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @NonNull
    private Date creationDate;

    @NonNull
    private Date expirationDate;

    @NonNull
    private int amount;

    public long getFarmId() {
        return farmId;
    }

    public void setFarmId(long farmId) {
        this.farmId = farmId;
    }

    public long getSackId() {
        return sackId;
    }

    public void setSackId(long sackId) {
        this.sackId = sackId;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    private String description;
}
