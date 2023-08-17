package com.habapp.ui.visit;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.habapp.MainActivity;
import com.habapp.R;
import com.habapp.databinding.FragmentNewVisitBinding;
import com.habapp.databinding.FragmentNewVisitBinding;
import com.habapp.models.Farm;
import com.habapp.models.Visit;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.FarmWithVisits;
import com.habapp.models.relations.VisitWithVegetables;
import com.habapp.models.relations.VisitWithVegetables;
import com.habapp.ui.farm.FarmViewModel;
import com.habapp.ui.vegetable.VegetableViewModel;
import com.habapp.utils.DatePickerFragment;
import com.habapp.utils.Location;
import com.habapp.utils.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class NewVisitFragment extends Fragment {

    private FragmentNewVisitBinding binding;
    private boolean[] selectedVegetables;
    private List<Vegetable> vegetablesList;
    private String[] vegetableNames;
    private VegetableViewModel vegetableViewModel;
    DialogFragment datePicker;
    private FarmViewModel farmViewModel;
    private VisitViewModel visitViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected boolean isDateValid(TextView textView) {
        return Validator.isDateValid(textView, "La fecha no puede estar vacía", "La fecha no es válida");
    }

    protected boolean isDescriptionValid(EditText editText) {
        return !Validator.isTooLong(editText, "La descripción debe tener un máximo de 255 caracteres.");
    }

    private void setSaveListener() {
        binding.buttonSave.setOnClickListener(view -> {

            if (!this.isDateValid(this.binding.data.editDate) || !this.isDescriptionValid(this.binding.data.editDescription)) {
                return;
            }

            Date date = null;
            try {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(this.binding.data.editDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String str = this.binding.data.editPersonName.getText().toString();
            List<String> nameList = Arrays.asList(str.split("\\s*,\\s*"));
            nameList = nameList.stream().map(String::trim).filter(name -> !name.isEmpty()).collect(Collectors.toList());

            Farm farm = (Farm) binding.spinnerFarmName.getSelectedItem();

            String description = this.binding.data.editDescription.getText().toString().trim();
            if (description.isEmpty()) {
                description = farm.getName() + " - " + this.binding.data.editDate.getText().toString();
            }

            Visit visit = new Visit(date, nameList,description);

            visit.setFarmId(farm.getFarmId());

            VisitWithVegetables visitWithVegetables = new VisitWithVegetables(visit, vegetablesList);

            VisitViewModel viewModel = new ViewModelProvider(this).get(VisitViewModel.class);
            viewModel.insert(visitWithVegetables);

            getActivity().onBackPressed();
        });
    }

    private void setSpinner() {
        this.farmViewModel = new ViewModelProvider(this).get(FarmViewModel.class);
        this.farmViewModel.getAllFarms().observe(getViewLifecycleOwner(), farms -> {
            Spinner spinner = binding.spinnerFarmName;
            farms.add(0,new Farm("Seleccione una quinta", "none",1,new Location(1,1)));
            ArrayAdapter<Farm> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, farms);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        binding.form.setVisibility(View.GONE);
                    } else {

                        farmViewModel.getFarmWithVisits(farms.get(i).getFarmId()).observe(getViewLifecycleOwner(), farmWithVisits -> {
                            Visit visit = farmWithVisits.getVisits().stream().sorted((Visit visit1, Visit visit2) -> visit1.getDate().before(visit2.getDate())?1:-1).findFirst().orElse(null);
                            if (visit == null) {
                                binding.data.editDate.setText("");
                                binding.data.editPersonName.setText("");
                                binding.data.editDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                                setVegetableDialog();
                                binding.form.setVisibility(View.VISIBLE);
                                return;
                            }

                            binding.data.editDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(visit.getDate()));
                            binding.data.editPersonName.setText(String.join(", ", visit.getVisitors()));
                            visitViewModel.getVisitWithVegetables(visit.getVisitId()).observe(getViewLifecycleOwner(), visitWithVegetables -> {
                                setVegetableDialog(visitWithVegetables);
                                binding.form.setVisibility(View.VISIBLE);
                            });
                        });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        });
    }

    private void setDatePicker() {
        this.datePicker = new DatePickerFragment(binding.data.editDate);
        binding.data.buttonDate.setOnClickListener(view -> {
            datePicker.show(getActivity().getSupportFragmentManager(), "datePicker");
        });
    }

    private void setVegetableDialog(VisitWithVegetables visitWithVegetables) {
        this.vegetableViewModel = new ViewModelProvider(this).get(VegetableViewModel.class);
        this.vegetablesList = visitWithVegetables.getAvailableVegetables();
        // initialize list of vegetables with all vegetables
        this.vegetableViewModel.getAllVegetables().observe(getViewLifecycleOwner(), vegetables -> {

            // initialize selected vegetables
            selectedVegetables = new boolean[vegetables.size()];
            for (int i = 0; i < vegetables.size(); i++) {
                String vegetableName = vegetables.get(i).getName();
                selectedVegetables[i] = visitWithVegetables.getAvailableVegetables().stream().anyMatch(v -> v.getName().equals(vegetableName));
            }

            // initialize list of vegetable names
            vegetableNames = vegetables.stream().map(Vegetable::getName).toArray(String[]::new);

            // set onClick listener for the text view
            binding.data.editAvailableVegetables.setOnClickListener(view -> {
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

    private void setVegetableDialog() {
        this.vegetableViewModel = new ViewModelProvider(this).get(VegetableViewModel.class);

        // initialize list of vegetables with all vegetables
        this.vegetableViewModel.getAllVegetables().observe(getViewLifecycleOwner(), vegetables -> {

            // initialize selected vegetables
            this.selectedVegetables = new boolean[vegetables.size()];

            // initialize list of vegetable names
            this.vegetableNames = vegetables.stream().map(Vegetable::getName).toArray(String[]::new);

            // set onClick listener for the text view
            binding.data.editAvailableVegetables.setOnClickListener(view -> {
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewVisitBinding.inflate(inflater, container, false);
        this.visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        this.setSpinner();
        this.setVegetableDialog();
        this.setDatePicker();
        this.setSaveListener();
        return binding.getRoot();
    }
}