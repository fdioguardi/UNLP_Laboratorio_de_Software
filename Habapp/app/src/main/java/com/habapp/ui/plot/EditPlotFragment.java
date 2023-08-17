package com.habapp.ui.plot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.habapp.MainActivity;
import com.habapp.databinding.FragmentEditPlotBinding;
import com.habapp.models.Plot;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.PlotWithVegetables;
import com.habapp.ui.vegetable.VegetableViewModel;

import java.util.ArrayList;


public class EditPlotFragment extends NewPlotFragment {

    private FragmentEditPlotBinding binding;
    private Plot plot;
    private PlotWithVegetables pwv;
    private MainActivity activity;

    private boolean[] selectedVegetables;
    private ArrayList<Vegetable> vegetablesList;
    private String[] vegetableNames;
    private VegetableViewModel vegetableViewModel;

    private void setFormValues(Plot plot) {
        this.binding.data.editName.setText(plot.getName());
        this.binding.data.editDescription.setText(plot.getDescription());
        this.binding.data.editAgroecologicArea.setText(String.valueOf(plot.getAgroecologicArea()));
        this.binding.data.editRows.setText(String.valueOf(plot.getRows()));
        this.binding.data.isCovered.setChecked(plot.isCovered());
        // LISTA DE VEGETALES
        this.setVegetableDialog(plot);
    }

    private void setVegetableDialog(Plot plot) {
        this.vegetableViewModel = new ViewModelProvider(this).get(VegetableViewModel.class);

        // initialize list of vegetables with all vegetables
        this.vegetableViewModel.getAllVegetables().observe(getViewLifecycleOwner(), vegetables -> {

            // initialize selected vegetables
            selectedVegetables = new boolean[vegetables.size()];
            for (int i = 0; i < vegetables.size(); i++) {
                String vegetableName = vegetables.get(i).getName();
                selectedVegetables[i] = pwv.getVegetables().stream().anyMatch(v -> v.getName().equals(vegetableName));
            }

            // initialize list of vegetable names
            vegetableNames = vegetables.stream().map(Vegetable::getName).toArray(String[]::new);

            // set onClick listener for the text view
            binding.data.textViewVegetables.setOnClickListener(view -> {
                // initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                // set title
                builder.setTitle("Seleccione los vegetales");

                // set dialog non cancelable
                builder.setCancelable(false);

                // set choices of dialog
                builder.setMultiChoiceItems(vegetableNames, this.selectedVegetables, (dialogInterface, i, b) -> selectedVegetables[i] = b);

                // set what happens when the dialog is accepted
                builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                    // set vegetable list of current plot in this.vegetableList
                    this.vegetablesList = new ArrayList<>();
                    for (int j = 0; j < selectedVegetables.length; j++) {
                        if (this.selectedVegetables[j]) {
                            this.vegetablesList.add(vegetables.get(j));
                        }
                    }
                });

                // set what happens when the dialog is canceled
                builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });

                // set what happens when the dialog is closed
                builder.setNeutralButton("Cerrar", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });

                // show the dialog
                builder.show();

            });

        });
    }

    private void setEditListener(Plot plot) {
        binding.buttonEdit.setOnClickListener(view -> {

            if (!isNameValid(binding.data.editName)
                    || !isDescValid(binding.data.editDescription)
                    || !areRowsValid(binding.data.editRows)
                    || !isAreaValid(binding.data.editAgroecologicArea)) {
                return;
            }

            plot.setName(this.binding.data.editName.getText().toString());
            plot.setDescription(this.binding.data.editDescription.getText().toString());
            plot.setAgroecologicArea(Double.parseDouble(this.binding.data.editAgroecologicArea.getText().toString()));
            plot.setRows(Integer.parseUnsignedInt(this.binding.data.editRows.getText().toString()));
            plot.setCovered(this.binding.data.isCovered.isChecked());
            pwv.setVegetables(this.vegetablesList);

            getActivity().onBackPressed();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditPlotBinding.inflate(inflater, container, false);

        int plotPosition = getArguments().getInt("position");
        this.activity = (MainActivity) getActivity();

        this.plot = this.activity.getPlots().getValue().get(plotPosition);
        this.pwv = this.activity.getPwvs().getValue()
                .stream()
                .filter(pwv -> pwv.getPlot().equals(this.plot))
                .findFirst().get();

        this.setFormValues(plot);
        this.setEditListener(plot);

        return binding.getRoot();
    }
}