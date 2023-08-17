package com.habapp.ui.visit;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.habapp.dbs.HabappRoomDatabase;
import com.habapp.models.Visit;
import com.habapp.models.Visit;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.VisitAvailableVegetableCrossRef;
import com.habapp.models.relations.VisitWithVegetables;
import com.habapp.repositories.VisitRepository;
import com.habapp.repositories.VegetableRepository;

import java.util.List;

public class VisitViewModel extends AndroidViewModel {
    private final VisitRepository repository;
    private final VegetableRepository vegetableRepository;

    public VisitViewModel(Application application) {
        super(application);
        this.repository = new VisitRepository(application);
        this.vegetableRepository = new VegetableRepository(application);
    }

    public void insert(Visit... visits) {
        this.repository.insert(visits);
    }

    public void update(Visit... visits) {
        this.repository.update(visits);
    }

    public void delete(Visit... visits) {
        this.repository.delete(visits);
    }

    public LiveData<Visit> findByVisitID(long visitId) {
        return this.repository.findByVisitID(visitId);
    }

    public void insert(VisitWithVegetables visitWithVegetables) {
        this.repository.insert(visitWithVegetables);
    }

    public void insert(VisitAvailableVegetableCrossRef... visitVegetableCrossRefs) {
        this.repository.insert(visitVegetableCrossRefs);
    }

    public void removeAvailableVegetableFromVisit(long visitId, long vegetableId) {
        this.repository.removeAvailableVegetableFromVisit(visitId, vegetableId);
    }

    public void removeVisitAvailableVegetableCrossRefs(long visitId) {
        this.repository.removeVisitAvailableVegetableCrossRefs(visitId);
    }

    public LiveData<VisitWithVegetables> getVisitWithVegetables(long visitId) {
        return this.repository.getVisitWithVegetables(visitId);
    }

    public void newAvailableVegetablesForVisit(Visit visit, List<Vegetable> vegetables) {
        this.repository.newAvailableVegetablesForVisit(visit, vegetables);
    }

    public LiveData<List<Visit>> getAllVisits() {
        return this.repository.getAllVisits();
    }
}
