package com.habapp.ui.sack;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.habapp.R;
import com.habapp.databinding.FragmentNewSackBinding;
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
import com.habapp.utils.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class NewSackFragment extends Fragment {

    DialogFragment creationDatePicker;
    DialogFragment expirationDatePicker;
    private FragmentNewSackBinding binding;
    private boolean[] selectedVegetables;
    private List<Vegetable> vegetablesList;
    private String[] vegetableNames;
    private FarmViewModel farmViewModel;
    private PlotViewModel plotViewModel;
    protected List<String> farmVegetables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected boolean isAmountValid(EditText editText) {
        return Validator.isPositiveInt(editText, "La cantidad debe ser un número natural.");
    }

    protected boolean isDateValid(TextView textView) {
        return Validator.isDateValid(textView, "La fecha no puede estar vacía", "La fecha no es válida");
    }

    protected boolean isDateOlderThanDate(TextView date1, TextView date2) {
        return Validator.isDateOlderThanDate(date1, date2, "La fecha de creación no puede ser posterior a la fecha de expiración");
    }

    protected boolean isDescriptionValid(EditText editText) {
        return !Validator.isTooLong(editText, "La descripción debe tener un máximo de 255 caracteres.");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected boolean areVegetablesValid(Sack sack, List<Vegetable> vegetables) {
        if (vegetables == null || vegetables.isEmpty()) {
            Toast.makeText(getContext(), "Debe ingrerar al menos un vegetal", Toast.LENGTH_LONG).show();
            return false;
        }

        List<String> missingVegetables = vegetables.stream().map(Vegetable::getName).filter(vegetable -> !this.farmVegetables.contains(vegetable)).collect(Collectors.toList());
        if (missingVegetables.size() > 2) {
            Toast.makeText(getContext(), "No puede agregar más de dos vegetales que no pertenezcan a la granja: " + String.join(", ", missingVegetables), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setSaveListener() {
        binding.buttonSave.setOnClickListener(view -> {

            if (!this.isDescriptionValid(this.binding.data.editDescription) || !this.isDateValid(this.binding.data.editCreationDate) || !this.isDateValid(this.binding.data.editExpirationDate) || !this.isDateOlderThanDate(this.binding.data.editCreationDate, this.binding.data.editExpirationDate) || !isAmountValid(this.binding.data.editAmount)) {
                return;
            }

            Date creationDate = null, expirationDate = null;
            try {
                creationDate = new SimpleDateFormat("dd/MM/yyyy").parse(this.binding.data.editCreationDate.getText().toString());
                expirationDate = new SimpleDateFormat("dd/MM/yyyy").parse(this.binding.data.editExpirationDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int amount = Integer.parseUnsignedInt(this.binding.data.editAmount.getText().toString());

            Farm farm = (Farm) binding.spinnerFarmName.getSelectedItem();

            String description = this.binding.data.editDescription.getText().toString().trim();
            if (description.isEmpty()) {
                description = farm.getName() + " - " + this.binding.data.editCreationDate.getText().toString();
            }

            Sack sack = new Sack(creationDate, expirationDate, amount, description);
            sack.setFarmId(farm.getFarmId());

            if (!areVegetablesValid(sack, vegetablesList)) {
                return;
            }

            SackWithVegetables sackWithVegetables = new SackWithVegetables(sack, vegetablesList);

            SackViewModel viewModel = new ViewModelProvider(this).get(SackViewModel.class);
            viewModel.insert(sackWithVegetables);

            getActivity().onBackPressed();
        });
    }

    private void setSpinner() {
        this.farmViewModel = new ViewModelProvider(this).get(FarmViewModel.class);
        this.plotViewModel = new ViewModelProvider(this).get(PlotViewModel.class);
        this.farmViewModel.getAllFarms().observe(getViewLifecycleOwner(), farms -> {
            Spinner spinner = binding.spinnerFarmName;
            farms.add(0, new Farm("Seleccione una quinta", "none", 1, new Location(1, 1)));
            ArrayAdapter<Farm> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, farms);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        binding.form.setVisibility(View.GONE);
                    } else {
                        binding.form.setVisibility(View.VISIBLE);
                        NewSackFragment.this.farmVegetables = new ArrayList<>();
                        NewSackFragment.this.farmViewModel.getFarmWithPlots(farms.get(i).getFarmId()).observe(getViewLifecycleOwner(), farmWithPlots -> {
                            for (Plot plot : farmWithPlots.getPlots()) {
                                NewSackFragment.this.plotViewModel.getPlotWithVegetables(plot.getPlotId()).observe(getViewLifecycleOwner(), plotWithVegetables -> {
                                    NewSackFragment.this.farmVegetables.addAll(plotWithVegetables.getVegetables().stream().map(Vegetable::getName).collect(Collectors.toList()));
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

    private void setDatePickers() {
        this.creationDatePicker = new DatePickerFragment(binding.data.editCreationDate);
        this.expirationDatePicker = new DatePickerFragment(binding.data.editExpirationDate);
        binding.data.buttonCreationDate.setOnClickListener(view -> {
            creationDatePicker.show(getActivity().getSupportFragmentManager(), "datePicker");
        });
        binding.data.buttonExpirationDate.setOnClickListener(view -> {
            expirationDatePicker.show(getActivity().getSupportFragmentManager(), "datePicker");
        });
    }

    private void setVegetableDialog() {
        VegetableViewModel vegetableViewModel = new ViewModelProvider(this).get(VegetableViewModel.class);

        // initialize list of vegetables with all vegetables
        vegetableViewModel.getAllVegetables().observe(getViewLifecycleOwner(), vegetables -> {

            // initialize selected vegetables
            this.selectedVegetables = new boolean[vegetables.size()];

            // initialize list of vegetable names
            this.vegetableNames = vegetables.stream().map(Vegetable::getName).toArray(String[]::new);

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
                    // set vegetable list of current visit in this.vegetableList
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewSackBinding.inflate(inflater, container, false);
        this.setSpinner();
        this.setVegetableDialog();
        this.setDatePickers();
        this.setSaveListener();
        return binding.getRoot();
    }
}