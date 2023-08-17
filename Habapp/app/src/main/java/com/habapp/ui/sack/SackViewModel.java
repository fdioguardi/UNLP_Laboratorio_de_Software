package com.habapp.ui.sack;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.habapp.models.Sack;
import com.habapp.models.Vegetable;
import com.habapp.models.Visit;
import com.habapp.models.relations.SackVegetableCrossRef;
import com.habapp.models.relations.SackWithVegetables;
import com.habapp.repositories.SackRepository;
import com.habapp.repositories.VegetableRepository;

import java.util.List;

public class SackViewModel extends AndroidViewModel {
    private final SackRepository repository;
    private final VegetableRepository vegetableRepository;

    public SackViewModel(Application application) {
        super(application);
        this.repository = new SackRepository(application);
        this.vegetableRepository = new VegetableRepository(application);
    }

    public void insert(Sack... sacks) {
        this.repository.insert(sacks);
    }

    public void update(Sack... sacks) {
        this.repository.update(sacks);
    }

    public void delete(Sack... sacks) {
        this.repository.delete(sacks);
    }

    public LiveData<Sack> findBySackID(long sackId) {
        return this.repository.findBySackID(sackId);
    }

    public void insert(SackVegetableCrossRef... sackVegetableCrossRefs) {
        this.repository.insert(sackVegetableCrossRefs);
    }

    public void removeSackVegetableCrossRefs(long sackId) {
        this.repository.removeSackVegetableCrossRefs(sackId);
    }

    public void removeVegetableFromSack(long sackId, long vegetableId) {
        this.repository.removeVegetableFromSack(sackId, vegetableId);
    }

    public LiveData<SackWithVegetables> getSackWithVegetables(long sackId) {
        return this.repository.getSackWithVegetables(sackId);
    }

    public void insert(SackWithVegetables sackWithVegetables) {
        this.repository.insert(sackWithVegetables);
    }

    public void newVegetablesForSack(Sack sack, List<Vegetable> vegetables) {
        this.repository.newVegetablesForSack(sack, vegetables);
    }

    public LiveData<List<Sack>> getAllSacks() {
        return this.repository.getAllSacks();
    }
}
