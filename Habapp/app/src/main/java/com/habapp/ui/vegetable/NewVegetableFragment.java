package com.habapp.ui.vegetable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.habapp.R;
import com.habapp.databinding.FragmentNewVegetableBinding;
import com.habapp.models.Vegetable;
import com.habapp.utils.SpanishMonth;
import com.habapp.utils.Validator;

import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NewVegetableFragment extends Fragment {

    private FragmentNewVegetableBinding binding;


    protected boolean isDescValid(EditText editText) {
        return Validator.isTextValid(editText,
                "La descripción no puede estar vacía.",
                "La descripción no puede tener más de 255 caracteres.");
    }

    protected boolean isNameValid(EditText editText) {
        return Validator.isTextValid(editText,
                "El nombre no puede estar vacío.",
                "El nombre no puede tener más de 255 caracteres.");
    }

    private void setSaveListener() {
        binding.buttonSave.setOnClickListener(view -> {

            if (!this.isNameValid(this.binding.data.editName) || !this.isDescValid(this.binding.data.editDescription)) {
                return;
            }

            SpanishMonth harvestMonth = (SpanishMonth) this.binding.data.harvestMonthSpinner.getSelectedItem();
            SpanishMonth sowingMonth = (SpanishMonth) this.binding.data.sewingMonthSpinner.getSelectedItem();

            Vegetable vegetable = new Vegetable(this.binding.data.editName.getText().toString(),
                    this.binding.data.editDescription.getText().toString(),
                    harvestMonth.getMonth(),
                    sowingMonth.getMonth());

            VegetableViewModel viewModel = new ViewModelProvider(this).get(VegetableViewModel.class);
            viewModel.insert(vegetable);

            Snackbar.make(view, "Se insertó el vegetal: " + this.binding.data.editName.getText(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            getActivity().onBackPressed();
        });
    }

    protected void setSpinner(Spinner spinner) {
        List<SpanishMonth> meses = Arrays.stream(Month.values()).map(month -> new SpanishMonth(month)).collect(Collectors.toList());
        spinner.setAdapter(new ArrayAdapter<>(this.getContext(), R.layout.support_simple_spinner_dropdown_item, meses));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNewVegetableBinding.inflate(inflater, container, false);
        this.setSpinner(this.binding.data.harvestMonthSpinner);
        this.setSpinner(this.binding.data.sewingMonthSpinner);
        this.setSaveListener();
        return binding.getRoot();
    }
}