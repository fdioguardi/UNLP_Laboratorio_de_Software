package com.habapp.ui.farm;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.habapp.R;
import com.habapp.databinding.FragmentDetailFarmBinding;
import com.habapp.models.Farm;
import com.habapp.models.Plot;
import com.habapp.models.relations.FarmWithPlots;
import com.habapp.ui.plot.list.PlotListAdapter;

import java.util.List;

public class DetailFarmFragment extends Fragment {

    private FragmentDetailFarmBinding binding;
    private long farmId;
    private FarmViewModel viewModel;

    private void setDetails(@NonNull Farm farm) {
        binding.showName.setText(farm.getName());
        binding.showAdress.setText(farm.getAddress());
        binding.showArea.setText(String.valueOf(farm.getArea()));
        binding.showLatitude.setText(String.valueOf(farm.getLocation().getLatitude()));
        binding.showLongitude.setText(String.valueOf(farm.getLocation().getLongitude()));
    }

    private void setRecyclerView(List<Plot> plots) {
        final PlotListAdapter adapter = new PlotListAdapter(new PlotListAdapter.PlotDiff(), R.id.action_nav_detail_farm_to_nav_detail_plot);
        this.binding.recyclerview.setAdapter(adapter);
        this.binding.recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter.submitList(plots);
    }

    private void setDeleteConfirmation(FarmWithPlots farmWithPlots) {
        binding.buttonDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(true);
            builder.setTitle("Eliminar " + farmWithPlots.getFarm().getName());
            builder.setMessage("¿Estás segurx de querer borrar la quinta?");
            builder.setPositiveButton("Borrar", (dialogInterface, i) -> {
                viewModel.delete(farmWithPlots.getFarm());
                getActivity().onBackPressed();
            });
            builder.setNegativeButton("Cancelar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void setEditListener() {
        binding.buttonEdit.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("farmId", farmId);
            Navigation.findNavController(view).navigate(R.id.action_nav_detail_farm_to_nav_edit_farm, bundle);
        });
    }

    private void setMapButton(Farm farm) {
        binding.buttonMap.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putFloat("latitude", (float) farm.getLocation().getLatitude());
            bundle.putFloat("longitude", (float) farm.getLocation().getLongitude());
            bundle.putString("name", farm.getName());
            Navigation.findNavController(view).navigate(R.id.action_nav_detail_farm_to_nav_map_farm, bundle);
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailFarmBinding.inflate(inflater, container, false);

        farmId = getArguments().getLong("farmId");

        viewModel = new ViewModelProvider(this).get(FarmViewModel.class);
        LiveData<FarmWithPlots> liveFarmWithPlots = viewModel.getFarmWithPlots(farmId);

        liveFarmWithPlots.observe(this.getViewLifecycleOwner(), farmWithPlots -> {
            if (farmWithPlots != null) {
                setDetails(farmWithPlots.getFarm());
                setRecyclerView(farmWithPlots.getPlots());
                setDeleteConfirmation(farmWithPlots);
                setEditListener();
                setMapButton(farmWithPlots.getFarm());
            }
        });

        return binding.getRoot();
    }
}