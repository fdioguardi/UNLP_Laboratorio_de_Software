package com.habapp.ui.sack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.habapp.R;
import com.habapp.databinding.FragmentEditSackBinding;
import com.habapp.models.Farm;
import com.habapp.models.Plot;
import com.habapp.models.Sack;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.SackWithVegetables;
import com.habapp.ui.farm.FarmViewModel;
import com.habapp.ui.plot.PlotViewModel;
import com.habapp.ui.vegetable.VegetableViewModel;
import com.habapp.utils.DatePickerFragment;
import com.habapp.utils.Location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EditSackFragment extends NewSackFragment {
    DialogFragment creationDatePicker;
    DialogFragment expirationDatePicker;
    private FragmentEditSackBinding binding;
    private boolean[] selectedVegetables;
    private List<Vegetable> vegetablesList;
    private String[] vegetableNames;
    private SackViewModel sackViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setFormValues(Sack sack) {
        this.binding.data.editCreationDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(sack.getCreationDate()));
        this.binding.data.editExpirationDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(sack.getExpirationDate()));
        this.binding.data.editAmount.setText(String.valueOf(sack.getAmount()));
        this.binding.data.editDescription.setText(sack.getDescription());
    }

    private void setDatePicker() {
        this.creationDatePicker = new DatePickerFragment(binding.data.editCreationDate);
        this.expirationDatePicker = new DatePickerFragment(binding.data.editExpirationDate);
        binding.data.buttonCreationDate.setOnClickListener(view -> creationDatePicker.show(getActivity().getSupportFragmentManager(), "datePicker"));
        binding.data.buttonExpirationDate.setOnClickListener(view -> expirationDatePicker.show(getActivity().getSupportFragmentManager(), "datePicker"));
    }

    private void setVegetableDialog(SackWithVegetables sackWithVegetables) {
        VegetableViewModel vegetableViewModel = new ViewModelProvider(this).get(VegetableViewModel.class);
        this.vegetablesList = sackWithVegetables.getVegetables();
        // initialize list of vegetables with all vegetables
        vegetableViewModel.getAllVegetables().observe(getViewLifecycleOwner(), vegetables -> {

            // initialize selected vegetables
            selectedVegetables = new boolean[vegetables.size()];
            for (int i = 0; i < vegetables.size(); i++) {
                String vegetableName = vegetables.get(i).getName();
                selectedVegetables[i] = sackWithVegetables.getVegetables().stream().anyMatch(v -> v.getName().equals(vegetableName));
            }

            // initialize list of vegetable names
            vegetableNames = vegetables.stream().map(Vegetable::getName).toArray(String[]::new);

            // set onClick listener for the text view
            binding.data.editVegetables.setOnClickListener(view -> {
                // initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                // set title
                builder.setTitle("Seleccione los vegetales");

                // set dialog non cancelable
                builder.setCancelable(false);

                // set choices of dialog
                builder.setMultiChoiceItems(vegetableNames, this.selectedVegetables, (dialogInterface, i, b) -> {
                    selectedVegetables[i] = b;
                    if (!farmVegetables.contains(vegetableNames[i])) {
                        Toast toast = Toast.makeText(getContext(), "El vegetal '" + vegetableNames[i] + "' no pertenece a la granja seleccionada", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

                // set what happens when the dialog is accepted
                builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                    // set vegetable list of current sack in this.vegetableList
                    this.vegetablesList = new ArrayList<>();
                    for (int j = 0; j < selectedVegetables.length; j++) {
                        if (this.selectedVegetables[j]) {
                            this.vegetablesList.add(vegetables.get(j));
                        }
                    }
                });

                // set what happens when the dialog is canceled
                builder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss());

                // set what happens when the dialog is closed
                builder.setNeutralButton("Cerrar", (dialogInterface, i) -> dialogInterface.dismiss());

                // show the dialog
                builder.show();

            });

        });
    }

    private void setEditListener(SackWithVegetables sackWithVegetables) {
        binding.buttonEdit.setOnClickListener(view -> {

            Sack sack = sackWithVegetables.getSack();

            Date creationDate = null, expirationDate = null;
            try {
                creationDate = new SimpleDateFormat("dd/MM/yyyy").parse(this.binding.data.editCreationDate.getText().toString());
                expirationDate = new SimpleDateFormat("dd/MM/yyyy").parse(this.binding.data.editExpirationDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (!this.isDateValid(this.binding.data.editCreationDate)
                    || !this.isDateValid(this.binding.data.editExpirationDate)
                    || !isAmountValid(this.binding.data.editAmount)
                    || !this.isDateOlderThanDate(this.binding.data.editCreationDate, this.binding.data.editExpirationDate)
                    || !this.isDescriptionValid(this.binding.data.editDescription)
            ) {
                return;
            }


            sack.setCreationDate(creationDate);
            sack.setExpirationDate(expirationDate);
            sack.setAmount(Integer.parseUnsignedInt(this.binding.data.editAmount.getText().toString()));

            Farm farm = (Farm) binding.spinnerFarmName.getSelectedItem();
            sack.setFarmId(farm.getFarmId());

            String description = this.binding.data.editDescription.getText().toString().trim();
            if (description.isEmpty()) {
                description = farm.getName() + " - " + this.binding.data.editCreationDate.getText().toString();
            }
            sack.setDescription(description);

            if (!areVegetablesValid(sack, vegetablesList)) {
                return;
            }

            sackViewModel.update(sack);
            sackViewModel.newVegetablesForSack(sack, this.vegetablesList);

            getActivity().onBackPressed();
        });
    }

    private void setSpinner(Long farmID) {
        FarmViewModel farmViewModel = new ViewModelProvider(this).get(FarmViewModel.class);
        PlotViewModel plotViewModel = new ViewModelProvider(this).get(PlotViewModel.class);
        farmViewModel.getAllFarms().observe(getViewLifecycleOwner(), farms -> {
            Spinner spinner = binding.spinnerFarmName;
            farms.add(0, new Farm("Seleccione una quinta", "none", 1, new Location(1, 1)));
            ArrayAdapter<Farm> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, farms);
            spinner.setAdapter(adapter);

            Optional<Farm> farmResult = farms.stream().filter(f -> f.getFarmId() == farmID).findFirst();
            spinner.setSelection(farms.indexOf(farmResult.get()));

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        binding.form.setVisibility(View.GONE);
                    } else {
                        binding.form.setVisibility(View.VISIBLE);
                        farmVegetables = new ArrayList<>();
                        farmViewModel.getFarmWithPlots(farms.get(i).getFarmId()).observe(getViewLifecycleOwner(), farmWithPlots -> {
                            for (Plot plot : farmWithPlots.getPlots()) {
                                plotViewModel.getPlotWithVegetables(plot.getPlotId()).observe(getViewLifecycleOwner(), plotWithVegetables -> {
                                    farmVegetables.addAll(plotWithVegetables.getVegetables().stream().map(Vegetable::getName).collect(Collectors.toList()));
                                });
                            }
                        });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditSackBinding.inflate(inflater, container, false);

        long sackPosition = getArguments().getLong("sackId");

        this.sackViewModel = new ViewModelProvider(this).get(SackViewModel.class);

        this.sackViewModel.getSackWithVegetables(sackPosition).observe(getViewLifecycleOwner(), sackWithVegetables -> {
            if (sackWithVegetables != null) {
                this.setSpinner(sackWithVegetables.getSack().getFarmId());
                this.setDatePicker();
                this.setFormValues(sackWithVegetables.getSack());
                this.setVegetableDialog(sackWithVegetables);
                this.setEditListener(sackWithVegetables);
            }
        });

        return binding.getRoot();
    }
}