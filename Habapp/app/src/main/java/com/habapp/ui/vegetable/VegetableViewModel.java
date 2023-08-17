package com.habapp.ui.vegetable;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.habapp.models.Vegetable;
import com.habapp.repositories.VegetableRepository;

import java.util.List;

public class VegetableViewModel extends AndroidViewModel {

    private final VegetableRepository repository;
    private final LiveData<List<Vegetable>> allVegetables;

    public VegetableViewModel(Application application) {
        super(application);
        this.repository = new VegetableRepository(application);
        this.allVegetables = this.repository.getAllVegetables();
    }

    public LiveData<List<Vegetable>> getAllVegetables() {
        return this.allVegetables;
    }

    public void insert(Vegetable vegetable) {
        repository.insert(vegetable);
    }

    public void update(Vegetable vegetable) {
        repository.update(vegetable);
    }

    public void delete(Vegetable vegetable) {
        repository.delete(vegetable);
    }

    public LiveData<Vegetable> findByVegetableName(String name) {
        return repository.findByVegetableName(name);
    }

    public LiveData<Vegetable> findByVegetableID(long id) {
        return repository.findByVegetableID(id);
    }

    public void removeVegetableReferences(long vegetableId) {
        repository.removeVegetableReferences(vegetableId);
    }
}