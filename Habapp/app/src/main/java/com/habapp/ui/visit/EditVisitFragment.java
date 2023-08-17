package com.habapp.ui.visit;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.habapp.R;
import com.habapp.databinding.FragmentEditVisitBinding;
import com.habapp.models.Farm;
import com.habapp.models.Plot;
import com.habapp.models.Vegetable;
import com.habapp.models.Visit;
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
import java.util.Optional;
import java.util.stream.Collectors;

public class EditVisitFragment extends Fragment {
    private FragmentEditVisitBinding binding;
    private boolean[] selectedVegetables;
    private List<Vegetable> vegetablesList;
    private String[] vegetableNames;
    private VegetableViewModel vegetableViewModel;
    private FragmentActivity activity;
    private VisitViewModel visitViewModel;
    DialogFragment datePicker;

    private FarmViewModel farmViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setFormValues(Visit visit) {
        this.binding.data.editDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(visit.getDate()));
        this.binding.data.editPersonName.setText(String.join(", ", visit.getVisitors()));
        this.binding.data.editDescription.setText(visit.getDescription());
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

    protected boolean isDateValid(TextView textView) {
        return Validator.isDateValid(textView, "La fecha no puede estar vacía", "La fecha no es válida");
    }

    protected boolean isDescriptionValid(EditText editText) {
        return !Validator.isTooLong(editText, "La descripción debe tener un máximo de 255 caracteres.");
    }

    private void setEditListener(VisitWithVegetables visitWithVegetables) {
        binding.buttonEdit.setOnClickListener(view -> {
            if (!this.isDateValid(this.binding.data.editDate) || !this.isDescriptionValid(this.binding.data.editDescription)) {
                return;
            }

            Visit visit = visitWithVegetables.getVisit();

            Date date = null;
            try {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(this.binding.data.editDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            visit.setDate(date);

            String str = this.binding.data.editPersonName.getText().toString();
            List<String> nameList = Arrays.asList(str.split("\\s*,\\s*"));
            nameList = nameList.stream().map(String::trim).filter(name -> !name.isEmpty()).collect(Collectors.toList());

            visit.setVisitors(nameList);

            Farm farm = (Farm) binding.spinnerFarmName.getSelectedItem();
            visit.setFarmId(farm.getFarmId());

            String description = this.binding.data.editDescription.getText().toString().trim();
            if (description.isEmpty()) {
                description = farm.getName() + " - " + this.binding.data.editDate.getText().toString();
            }
            visit.setDescription(description);

            visitViewModel.update(visit);
            visitViewModel.newAvailableVegetablesForVisit(visit, this.vegetablesList);

            getActivity().onBackPressed();
        });
    }

    private void setSpinner(Long farmID) {
        this.farmViewModel = new ViewModelProvider(this).get(FarmViewModel.class);
        this.farmViewModel.getAllFarms().observe(getViewLifecycleOwner(), farms -> {
            Spinner spinner = binding.spinnerFarmName;
            farms.add(0,new Farm("Seleccione una quinta", "none",1,new Location(1,1)));
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
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditVisitBinding.inflate(inflater, container, false);

        long visitPosition = getArguments().getLong("visitId");
        this.activity = getActivity();

        this.visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);

        this.visitViewModel.getVisitWithVegetables(visitPosition).observe(getViewLifecycleOwner(), visitWithVegetables -> {
            if (visitWithVegetables != null) {
                this.setSpinner(visitWithVegetables.getVisit().getFarmId());
                this.setDatePicker();
                this.setFormValues(visitWithVegetables.getVisit());
                this.setVegetableDialog(visitWithVegetables);
                this.setEditListener(visitWithVegetables);
            }
        });

        return binding.getRoot();
    }
}