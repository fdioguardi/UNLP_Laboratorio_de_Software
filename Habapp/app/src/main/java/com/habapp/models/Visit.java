package com.habapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.habapp.models.converters.DateConverter;
import com.habapp.models.converters.StringListConverter;

import java.util.Date;
import java.util.List;

@Entity(tableName = "visit")
@TypeConverters({DateConverter.class, StringListConverter.class})
public class Visit {
    public long getVisitId() {
        return visitId;
    }

    public void setVisitId(long visitId) {
        this.visitId = visitId;
    }

    @PrimaryKey(autoGenerate = true)
    private long visitId;

    public long getFarmId() {
        return farmId;
    }

    @NonNull
    public List<String> getVisitors() {
        return visitors;
    }

    public void setVisitors(@NonNull List<String> visitors) {
        this.visitors = visitors;
    }

    public void setFarmId(long farmId) {
        this.farmId = farmId;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    public Visit(@NonNull Date date, @NonNull List<String> visitors, @NonNull String description) {
        this.date = date;
        this.visitors = visitors;
        this.description = description;
    }

    private long farmId;
    @NonNull
    private Date date;
    @NonNull
    private List<String> visitors;

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
