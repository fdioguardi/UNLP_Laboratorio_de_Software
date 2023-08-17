package com.habapp.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.habapp.daos.SackDao;
import com.habapp.daos.VisitDao;
import com.habapp.dbs.HabappRoomDatabase;
import com.habapp.models.Sack;
import com.habapp.models.Vegetable;
import com.habapp.models.Visit;
import com.habapp.models.relations.SackVegetableCrossRef;
import com.habapp.models.relations.SackWithVegetables;

import java.util.List;

public class SackRepository {
    private final SackDao mSackDao;
    private final LiveData<List<Sack>> mAllSacks;

    public SackRepository(Application application) {
        HabappRoomDatabase db = HabappRoomDatabase.getDatabase(application);
        this.mSackDao = db.sackDao();
        this.mAllSacks = mSackDao.getChronologically();
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Sack... sacks) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mSackDao.insert(sacks));
    }

    public void update(Sack... sacks) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mSackDao.update(sacks));
    }

    public void delete(Sack... sacks) {
        for (Sack sack : sacks) {
            this.removeSackVegetableCrossRefs(sack.getSackId());
        }
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mSackDao.delete(sacks));
    }

    public LiveData<Sack> findBySackID(long sackId) {
        return mSackDao.findBySackId(sackId);
    }

    public LiveData<List<Sack>> getAllSacks() { return this.mAllSacks; }

    /* Sack - Vegetable methods*/

    public void insert(SackVegetableCrossRef... sackVegetableCrossRefs) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mSackDao.insert(sackVegetableCrossRefs));
    }

    public void removeSackVegetableCrossRefs(long sackId) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mSackDao.removeSackVegetableCrossRefs(sackId));
    }

    public void removeVegetableFromSack(long sackId, long vegetableId) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mSackDao.removeVegetableFromSack(sackId, vegetableId));
    }

    public LiveData<SackWithVegetables> getSackWithVegetables(long sackId) {
        return mSackDao.getSackWithVegetables(sackId);
    }

    public void insert(SackWithVegetables sackWithVegetables) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mSackDao.insert(sackWithVegetables));
    }

    public void newVegetablesForSack(Sack sack, List<Vegetable> vegetables) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mSackDao.newVegetablesForSack(sack, vegetables));
    }


}
