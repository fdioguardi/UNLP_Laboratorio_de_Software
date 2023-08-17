package com.habapp.ui.plot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.habapp.R;
import com.habapp.databinding.FragmentDetailPlotBinding;
import com.habapp.models.Plot;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.PlotWithVegetables;
import com.habapp.ui.vegetable.list.VegetableListAdapter;

import java.util.List;


public class DetailPlotFragment extends Fragment {

    private FragmentDetailPlotBinding binding;

    private void setDetails(Plot plot) {
        this.binding.editName.setText(plot.getName());
        this.binding.editDescription.setText(plot.getDescription());
        this.binding.editAgroecologicArea.setText(String.valueOf(plot.getAgroecologicArea()));
        this.binding.editRows.setText(String.valueOf(plot.getRows()));
        this.binding.isCovered.setChecked(plot.isCovered());
    }

    private void setRecyclerView(List<Vegetable> vegetables) {
        final VegetableListAdapter adapter = new VegetableListAdapter(new VegetableListAdapter.VegetableDiff(), R.id.action_nav_detail_plot_to_fragment_detail_vegetable);
        this.binding.recyclerview.setAdapter(adapter);
        this.binding.recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter.submitList(vegetables);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailPlotBinding.inflate(inflater, container, false);

        long plotID = getArguments().getLong("plotId");

        PlotViewModel viewModel = new ViewModelProvider(this).get(PlotViewModel.class);
        LiveData<PlotWithVegetables> livePlotWithVegetables = viewModel.getPlotWithVegetables(plotID);

        livePlotWithVegetables.observe(this.getViewLifecycleOwner(), plotWithVegetables -> {
            if (plotWithVegetables != null) {
                this.setDetails(plotWithVegetables.getPlot());
                this.setRecyclerView(plotWithVegetables.getVegetables());
            }
        });

        return binding.getRoot();
    }
}