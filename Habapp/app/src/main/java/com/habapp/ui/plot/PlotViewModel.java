package com.habapp.ui.plot;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.habapp.dbs.HabappRoomDatabase;
import com.habapp.models.Plot;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.PlotVegetableCrossRef;
import com.habapp.models.relations.PlotWithVegetables;
import com.habapp.repositories.PlotRepository;
import com.habapp.repositories.VegetableRepository;

import java.util.List;

public class PlotViewModel extends AndroidViewModel {
    private final PlotRepository repository;
    private final VegetableRepository vegetableRepository;

    public PlotViewModel(Application application) {
        super(application);
        this.repository = new PlotRepository(application);
        this.vegetableRepository = new VegetableRepository(application);
    }

    public void insert(Plot... plots) {
        this.repository.insert(plots);
    }

    public void update(Plot... plots) {
        this.repository.update(plots);
    }

    public void delete(Plot... plots) {
        this.repository.delete(plots);
    }

    public LiveData<Plot> findByPlotID(long plotId) {
        return this.repository.findByPlotID(plotId);
    }

    public void insert(PlotVegetableCrossRef... plotVegetableCrossRefs) {
        this.repository.insert(plotVegetableCrossRefs);
    }

    public void removePlotVegetableCrossRefs(long plotId) {
        this.repository.removePlotVegetableCrossRefs(plotId);
    }

    public void removeVegetableFromPlot(long plotId, long vegetableId) {
        this.repository.removeVegetableFromPlot(plotId, vegetableId);
    }

    public LiveData<PlotWithVegetables> getPlotWithVegetables(long plotId) {
        return this.repository.getPlotWithVegetables(plotId);
    }

    public void insert(PlotWithVegetables plotWithVegetables) {
        this.repository.insert(plotWithVegetables);
    }

    public void newVegetablesForPlot(Plot plot, List<Vegetable> vegetables) {
        this.repository.newVegetablesForPlot(plot, vegetables);
    }
}
