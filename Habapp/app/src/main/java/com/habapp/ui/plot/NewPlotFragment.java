package com.habapp.ui.plot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.habapp.MainActivity;
import com.habapp.databinding.FragmentNewPlotBinding;
import com.habapp.models.Plot;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.PlotWithVegetables;
import com.habapp.ui.vegetable.VegetableViewModel;
import com.habapp.utils.Validator;

import java.util.ArrayList;

public class NewPlotFragment extends Fragment {

    private FragmentNewPlotBinding binding;
    private boolean[] selectedVegetables;
    private ArrayList<Vegetable> vegetablesList;
    private String[] vegetableNames;
    private VegetableViewModel vegetableViewModel;


    protected boolean isNameValid(EditText editText) {
        return Validator.isTextValid(editText,
                "El nombre no puede estar vacío.",
                "El nombre no puede tener más de 255 caracteres.");
    }

    protected boolean isDescValid(EditText editText) {
        return Validator.isTextValid(editText,
                "La descripción no puede estar vacía.",
                "La descripción no puede tener más de 255 caracteres.");
    }

    protected boolean isAreaValid(EditText editText) {
        return Validator.isDoubleValid(editText,
                "El area no puede estar vacía",
                "El area no puede tener más de 64 dígitos",
                "El área debe ser un número.");
    }

    protected boolean areRowsValid(EditText editText) {
        return Validator.isPositiveIntValid(editText,
                "Las filas no pueden estar vacías",
                "Las filas no pueden tener más de 64 dígitos",
                "Las filas deben ser un número natural.");
    }

    private void setSaveListener() {
        binding.buttonSave.setOnClickListener(view -> {

            if (!isNameValid(binding.data.editName)
                    || !isDescValid(binding.data.editDescription)
                    || !areRowsValid(binding.data.editRows)
                    || !isAreaValid(binding.data.editAgroecologicArea)) {
                return;
            }

            Plot plot = new Plot(this.binding.data.editName.getText().toString(),
                    this.binding.data.editDescription.getText().toString(),
                    Double.parseDouble(this.binding.data.editAgroecologicArea.getText().toString()),
                    Integer.parseUnsignedInt(this.binding.data.editRows.getText().toString()),
                    this.binding.data.isCovered.isChecked()
            );

            MainActivity activity = (MainActivity) getActivity();
            activity.addPlot(plot);

            // register this plot is asociated with the selected vegetables
            activity.addPwv(new PlotWithVegetables(plot, vegetablesList));

            getActivity().onBackPressed();
        });
    }

    private void setVegetableDialog() {
        this.vegetableViewModel = new ViewModelProvider(this).get(VegetableViewModel.class);

        // initialize list of vegetables with all vegetables
        this.vegetableViewModel.getAllVegetables().observe(getViewLifecycleOwner(), vegetables -> {

            // initialize selected vegetables
            this.selectedVegetables = new boolean[vegetables.size()];

            // initialize list of vegetable names
            this.vegetableNames = vegetables.stream().map(Vegetable::getName).toArray(String[]::new);

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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewPlotBinding.inflate(inflater, container, false);

        this.setVegetableDialog();
        this.setSaveListener();
        return binding.getRoot();
    }
}