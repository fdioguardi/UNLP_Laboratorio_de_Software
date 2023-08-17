package com.habapp.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.habapp.models.Vegetable;

import java.util.List;

@Dao
public interface VegetableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Vegetable... vegetables);

    @Delete()
    void delete(Vegetable... vegetables);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Vegetable... vegetables);

    @Query("DELETE FROM vegetable")
    void deleteAll();

    @Query("SELECT * FROM vegetable ORDER BY name ASC")
    LiveData<List<Vegetable>> getAlphabetically();

    @Query("SELECT * FROM vegetable WHERE name = :name")
    LiveData<Vegetable> findByVegetableName(String name);

    @Query("SELECT * FROM vegetable WHERE vegetableId = :vegetableId")
    LiveData<Vegetable> findByVegetableID(long vegetableId);

    @Query("DELETE FROM plot_vegetable_cross_ref WHERE vegetableId = :vegetableId")
    void removeVegetableReferences(long vegetableId);
}
