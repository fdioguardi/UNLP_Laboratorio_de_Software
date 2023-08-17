package com.habapp.ui.farm;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.habapp.models.Farm;
import com.habapp.models.Plot;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.FarmWithPlots;
import com.habapp.models.relations.FarmWithVisits;
import com.habapp.models.relations.PlotWithVegetables;
import com.habapp.repositories.FarmRepository;

import java.util.List;

public class FarmViewModel extends AndroidViewModel {
    private final FarmRepository repository;
    private final LiveData<List<Farm>> allFarms;

    public FarmViewModel(Application application) {
        super(application);
        this.repository = new FarmRepository(application);
        this.allFarms = this.repository.getAllFarms();
    }

    public void insert(Farm farm) {
        this.repository.insert(farm);
    }

    public void insert(Farm farm, List<PlotWithVegetables> plots) {
        this.repository.insert(farm, plots);
    }

    public void update(Farm farm) {
        this.repository.update(farm);
    }

    public void delete(Farm farm) {
        this.repository.delete(farm);
    }

    public LiveData<Farm> findByFarmId(long farmId) {
        return this.repository.findByFarmId(farmId);
    }

    public LiveData<FarmWithPlots> getFarmWithPlots(long farmId) {
        return this.repository.getFarmWithPlots(farmId);
    }

    public LiveData<List<Farm>> getAllFarms() {
        return this.repository.getAllFarms();
    }

    public LiveData<FarmWithVisits> getFarmWithVisits(long farmId) {
        return this.repository.getFarmWithVisits(farmId);
    }
}
