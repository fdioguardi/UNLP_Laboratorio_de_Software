package com.habapp.ui.vegetable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.habapp.databinding.FragmentEditVegetableBinding;
import com.habapp.models.Vegetable;
import com.habapp.utils.SpanishMonth;

import java.time.Month;

public class EditVegetableFragment extends NewVegetableFragment {

    private FragmentEditVegetableBinding binding;
    private VegetableViewModel viewModel;

    private void setFormValues(Vegetable vegetable) {
        this.binding.data.editName.setText(vegetable.getName());
        this.binding.data.editDescription.setText(vegetable.getDescription());

        this.setSpinner(this.binding.data.harvestMonthSpinner);
        this.setSpinner(this.binding.data.sewingMonthSpinner);

        this.binding.data.harvestMonthSpinner.setSelection(vegetable.getHarvest_month().ordinal());
        this.binding.data.sewingMonthSpinner.setSelection(vegetable.getSowing_month().ordinal());
    }

    private void setEditListener(Vegetable vegetable) {
        binding.buttonEdit.setOnClickListener(view -> {
            if (!this.isNameValid(this.binding.data.editName) || !this.isDescValid(this.binding.data.editDescription)) {
                return;
            }

            vegetable.setName(this.binding.data.editName.getText().toString());
            vegetable.setDescription(this.binding.data.editDescription.getText().toString());

            SpanishMonth harvestMonth = (SpanishMonth) this.binding.data.harvestMonthSpinner.getSelectedItem();
            SpanishMonth sowingMonth = (SpanishMonth) this.binding.data.sewingMonthSpinner.getSelectedItem();
            vegetable.setHarvest_month(harvestMonth.getMonth());
            vegetable.setSowing_month(sowingMonth.getMonth());

            viewModel.update(vegetable);

            Snackbar.make(view, "Se editó el vegetal: " + this.binding.data.editName.getText(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            getActivity().onBackPressed();
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditVegetableBinding.inflate(inflater, container, false);

        long vegetableId = getArguments().getLong("vegetableId");

        this.viewModel = new ViewModelProvider(this).get(VegetableViewModel.class);
        LiveData<Vegetable> liveVegetable = viewModel.findByVegetableID(vegetableId);

        liveVegetable.observe(this.getViewLifecycleOwner(), vegetable -> {
            if (vegetable != null) {
                setFormValues(vegetable);
                setEditListener(vegetable);
            }
        });

        return binding.getRoot();
    }
}