package com.habapp.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.habapp.daos.VisitDao;
import com.habapp.daos.VisitDao;
import com.habapp.dbs.HabappRoomDatabase;
import com.habapp.models.Visit;
import com.habapp.models.Visit;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.VisitAvailableVegetableCrossRef;
import com.habapp.models.relations.VisitWithVegetables;

import java.util.List;

public class VisitRepository {
    private final VisitDao mVisitDao;
    private final LiveData<List<Visit>> mAllVisits;

    public VisitRepository(Application application) {
        HabappRoomDatabase db = HabappRoomDatabase.getDatabase(application);
        this.mVisitDao = db.visitDao();
        mAllVisits = mVisitDao.getChronologically();
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Visit... visits) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mVisitDao.insert(visits));
    }

    public void update(Visit... visits) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mVisitDao.update(visits));
    }

    public void delete(Visit... visits) {
        for (Visit visit : visits) {
            this.removeVisitAvailableVegetableCrossRefs(visit.getVisitId());
        }
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mVisitDao.delete(visits));
    }

    public LiveData<Visit> findByVisitID(long visitId) {
        return mVisitDao.findByVisitId(visitId);
    }

    /* Visit - Vegetable methods*/

    public void insert(VisitAvailableVegetableCrossRef... visitVegetableCrossRefs) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mVisitDao.insert(visitVegetableCrossRefs));
    }



    public void removeVisitAvailableVegetableCrossRefs(long visitId) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mVisitDao.removeVisitAvailableVegetableCrossRefs(visitId));
    }

    public void removeAvailableVegetableFromVisit(long visitId, long vegetableId) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mVisitDao.removeAvailableVegetableFromVisit(visitId, vegetableId));
    }

    public LiveData<VisitWithVegetables> getVisitWithVegetables(long visitId) {
        return mVisitDao.getVisitWithVegetables(visitId);
    }

    public void insert(VisitWithVegetables visitWithVegetables) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mVisitDao.insert(visitWithVegetables));
    }

    public void newAvailableVegetablesForVisit(Visit visit, List<Vegetable> vegetables) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mVisitDao.newAvailableVegetablesForVisit(visit, vegetables));
    }

    public LiveData<List<Visit>> getAllVisits() {
        return mAllVisits;
    }
}
