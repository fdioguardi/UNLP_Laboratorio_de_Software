package com.habapp.ui.farm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.habapp.MainActivity;
import com.habapp.R;
import com.habapp.databinding.FragmentNewFarmBinding;
import com.habapp.models.Farm;
import com.habapp.models.Plot;
import com.habapp.models.relations.PlotWithVegetables;
import com.habapp.ui.plot.list.PlotInnerAdapter;
import com.habapp.utils.Location;
import com.habapp.utils.Validator;

import java.util.ArrayList;

public class NewFarmFragment extends Fragment {

    private MainActivity activity;
    private FragmentNewFarmBinding binding;
    private PlotInnerAdapter adapter;

    protected boolean isNameValid(EditText editText) {
        return Validator.isTextValid(editText,
                "El nombre no puede estar vacío.",
                "El nombre no puede tener más de 255 caracteres.");
    }

    protected boolean isAddressValid(EditText editText) {
        return Validator.isTextValid(editText,
                "La dirección no puede estar vacía.",
                "La dirección no puede tener más de 255 caracteres.");
    }

    protected boolean isAreaValid(EditText editText) {
        return Validator.isDoubleValid(editText,
                "El area no puede estar vacía",
                "El area no puede tener más de 64 dígitos",
                "El área debe ser un número.");
    }

    private void setSaveListener() {
        binding.buttonSave.setOnClickListener(view -> {

            if (!this.isNameValid(this.binding.data.editName)
                    || !this.isAddressValid(this.binding.data.editAdress)
                    || !this.isAreaValid(this.binding.data.editArea)) {
                return;
            }

            Location location = new Location(
                    Double.parseDouble(this.binding.data.latitudeLabel.getText().toString()),
                    Double.parseDouble(this.binding.data.longitudeLabel.getText().toString()));

            Farm farm = new Farm(this.binding.data.editName.getText().toString(),
                    this.binding.data.editAdress.getText().toString(),
                    Double.parseDouble(this.binding.data.editArea.getText().toString()),
                    location);

            FarmViewModel farmViewModel = new ViewModelProvider(this).get(FarmViewModel.class);
            farmViewModel.insert(farm, activity.getPwvs().getValue());

            Snackbar.make(view, "Se insertó la quinta: " + this.binding.data.editName.getText(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            activity.resetLocation();

            getActivity().onBackPressed();
        });
    }

    private void setAddPlotListener() {
        binding.data.buttonAddPlot.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_nav_new_farm_to_nav_new_plot)
        );
    }

    private void setLocationAndButton() {
        this.activity.getLocation().observe(this.getViewLifecycleOwner(), location -> {
            this.binding.data.latitudeLabel.setText(String.valueOf(location.getLatitude()));
            this.binding.data.longitudeLabel.setText(String.valueOf(location.getLongitude()));
        });

        binding.data.buttonMap.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_nav_new_farm_to_nav_map_location_farm);
        });
    }

    private void setRecyclerView() {
        adapter = new PlotInnerAdapter(new PlotInnerAdapter.PlotDiff());
        this.binding.data.recyclerview.setAdapter(adapter);
        this.binding.data.recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));

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
        for (PlotWithVegetables pwv : this.activity.getPwvs().getValue()) {
            if (pwv.getPlot().equals(removedPlot)) {
                this.activity.getPwvs().getValue().remove(pwv);
                break;
            }
        }
        adapter.notifyItemRemoved(position);
    }

    public void changeItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        Navigation.findNavController(this.getParentFragment().getView()).navigate(R.id.action_nav_new_farm_to_nav_edit_plot, bundle);

        adapter.notifyItemChanged(position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = (MainActivity) this.getActivity();
        activity.clearPlots();
        activity.setPwvs(new ArrayList<PlotWithVegetables>());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentNewFarmBinding.inflate(inflater, container, false);
        this.setRecyclerView();
        this.setAddPlotListener();
        this.setSaveListener();
        this.setLocationAndButton();
        return this.binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}