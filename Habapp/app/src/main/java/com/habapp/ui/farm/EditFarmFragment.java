package com.habapp.ui.farm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.habapp.MainActivity;
import com.habapp.R;
import com.habapp.databinding.FragmentEditFarmBinding;
import com.habapp.models.Farm;
import com.habapp.models.Plot;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.FarmWithPlots;
import com.habapp.models.relations.PlotVegetableCrossRef;
import com.habapp.models.relations.PlotWithVegetables;
import com.habapp.ui.plot.PlotViewModel;
import com.habapp.ui.plot.list.PlotInnerAdapter;
import com.habapp.utils.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditFarmFragment extends NewFarmFragment {

    private FragmentEditFarmBinding binding;
    private FarmViewModel viewModel;
    private MainActivity activity;
    private boolean firstTime;
    private PlotInnerAdapter adapter;
    private List<Plot> removedPlots;

    private void setFormValues(Farm farm) {
        this.binding.data.editName.setText(farm.getName());
        this.binding.data.editArea.setText(String.valueOf(farm.getArea()));
        this.binding.data.editAdress.setText(farm.getAddress());
        this.binding.data.latitudeLabel.setText(String.valueOf(farm.getLocation().getLatitude()));
        this.binding.data.longitudeLabel.setText(String.valueOf(farm.getLocation().getLongitude()));
    }

    private void setEditListener(Farm farm) {
        binding.buttonEdit.setOnClickListener(view -> {

            if (!this.isNameValid(this.binding.data.editName)
                    || !this.isAddressValid(this.binding.data.editAdress)
                    || !this.isAreaValid(this.binding.data.editArea)) {
                return;
            }

            Location location = new Location(
                    Double.parseDouble(this.binding.data.latitudeLabel.getText().toString()),
                    Double.parseDouble(this.binding.data.longitudeLabel.getText().toString()));

            farm.setName(this.binding.data.editName.getText().toString());
            farm.setAddress(this.binding.data.editAdress.getText().toString());
            farm.setArea(Double.parseDouble(this.binding.data.editArea.getText().toString()));
            farm.setLocation(location);

            viewModel.update(farm);

            PlotViewModel plotViewModel = new ViewModelProvider(this).get(PlotViewModel.class);

            // actualiza las plots existentes
            List<Plot> goodOldPlots = new ArrayList<>();
            for (Plot plot : Objects.requireNonNull(activity.getPlots().getValue())) {
                if (plot.getFarmId() == farm.getFarmId()) { // if the plot is already in the db
                    goodOldPlots.add(plot);
                }
            }
            plotViewModel.update(goodOldPlots.toArray(new Plot[0]));


            // crea las nuevas relaciones entre plot y verdura
            for (PlotWithVegetables pwv : Objects.requireNonNull(activity.getPwvs().getValue())) {
                if (pwv.getPlot().getFarmId() == farm.getFarmId()) { // if the plot is already in the db
                    plotViewModel.newVegetablesForPlot(pwv.getPlot(), pwv.getVegetables());
                } else {  //agrega el plot y sus relaciones a la db
                    pwv.getPlot().setFarmId(farm.getFarmId());
                    plotViewModel.insert(pwv);
                }
            }

            // elimina de la db las plots borradas
            plotViewModel.delete(removedPlots.toArray(new Plot[0]));
            for (Plot plot : removedPlots) {
                // remove every relation between plot and vegetable of this farm
                plotViewModel.removePlotVegetableCrossRefs(plot.getPlotId());
            }

            Snackbar.make(view, "Se editó la quinta: " + this.binding.data.editName.getText(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            activity.resetLocation();

            getActivity().onBackPressed();
        });
    }

    private void setLocationAndButton(Location location) {
        this.activity.getLocation().observe(this.getViewLifecycleOwner(), loc -> {
            this.binding.data.latitudeLabel.setText(String.valueOf(loc.getLatitude()));
            this.binding.data.longitudeLabel.setText(String.valueOf(loc.getLongitude()));
        });

        binding.data.buttonMap.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_nav_edit_farm_to_nav_map_location_farm);
        });
    }

    private void setRecyclerView(List<Plot> plots) {
        adapter = new PlotInnerAdapter(new PlotInnerAdapter.PlotDiff());
        this.binding.data.recyclerview.setAdapter(adapter);
        this.binding.data.recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter.submitList(plots);
        this.activity.getPlots().observe(this.getViewLifecycleOwner(), adapter::submitList);

        adapter.setOnItemClickListener(new PlotInnerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    public void removeItem(int position) {
        Plot removedPlot = this.activity.getPlots().getValue().remove(position);
        removedPlots.add(removedPlot);

        PlotWithVegetables removedPwv = this.activity.getPwvs().getValue().stream()
                .filter(pwv -> pwv.getPlot().equals(removedPlot))
                .findFirst().get();
        this.activity.getPwvs().getValue().remove(removedPwv);

        adapter.notifyItemRemoved(position);
    }

    public void changeItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        Navigation.findNavController(this.getParentFragment().getView()).navigate(R.id.action_nav_edit_farm_to_nav_edit_plot, bundle);

        adapter.notifyItemChanged(position);
    }

    private void setAddPlotListener() {
        binding.data.buttonAddPlot.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_nav_edit_farm_to_nav_new_plot)
        );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.firstTime = true;
        this.removedPlots = new ArrayList<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        long farmId = getArguments().getLong("farmId");

        activity = (MainActivity) this.getActivity();

        this.viewModel = new ViewModelProvider(this).get(FarmViewModel.class);
        PlotViewModel plotViewModel = new ViewModelProvider(this).get(PlotViewModel.class);

        LiveData<FarmWithPlots> liveFarmWithPlots = viewModel.getFarmWithPlots(farmId);

        liveFarmWithPlots.observe(this.getViewLifecycleOwner(), farmWithPlots -> {

            if (farmWithPlots != null) {
                if (firstTime) {
                    List<Plot> plots = farmWithPlots.getPlots();
                    List<PlotWithVegetables> pwvs = new ArrayList<>();
                    this.activity.setPlots(plots);
                    for (Plot p : plots) {
                        plotViewModel.getPlotWithVegetables(p.getPlotId()).observe(this.getViewLifecycleOwner(), pwv -> {
                            pwvs.add(pwv);
                        });
                    }
                    this.activity.setPwvs(pwvs);

                    this.activity.setLocation(farmWithPlots.getFarm().getLocation());
                    firstTime = false;
                }

                setFormValues(farmWithPlots.getFarm());
                setEditListener(farmWithPlots.getFarm());
                setLocationAndButton(farmWithPlots.getFarm().getLocation());
                setRecyclerView(farmWithPlots.getPlots());
                setAddPlotListener();
            }
        });

        binding = FragmentEditFarmBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}