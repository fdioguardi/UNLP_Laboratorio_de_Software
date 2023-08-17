package com.habapp.ui.vegetable;

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

import com.habapp.R;
import com.habapp.databinding.FragmentDetailVegetableBinding;
import com.habapp.models.Vegetable;
import com.habapp.utils.SpanishMonth;


public class DetailVegetableFragment extends Fragment {

    private FragmentDetailVegetableBinding binding;
    private long vegetableId;
    private VegetableViewModel viewModel;

    private void setDetails(Vegetable vegetable) {
        binding.showName.setText(vegetable.getName());
        binding.showDescription.setText(vegetable.getDescription());
        SpanishMonth harvestMonth = new SpanishMonth(vegetable.getHarvest_month());
        binding.showHarvestMonth.setText(harvestMonth.toString());
        SpanishMonth sowingMonth = new SpanishMonth(vegetable.getSowing_month());
        binding.showSowingMonth.setText(sowingMonth.toString());
    }

    private void setDeleteConfirmation(Vegetable vegetable) {
        binding.buttonDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(true);
            builder.setTitle("Eliminar " + vegetable.getName());
            builder.setMessage("¿Estás segurx de querer borrar la verdura?");
            builder.setPositiveButton("Borrar", (dialogInterface, i) -> {
                viewModel.delete(vegetable);
                viewModel.removeVegetableReferences(vegetableId);
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
            bundle.putLong("vegetableId", vegetableId);
            Navigation.findNavController(view).navigate(R.id.action_fragment_detail_vegetable_to_fragment_edit_vegetable, bundle);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailVegetableBinding.inflate(inflater, container, false);

        vegetableId = getArguments().getLong("vegetableId");

        viewModel = new ViewModelProvider(this).get(VegetableViewModel.class);
        LiveData<Vegetable> liveVegetable = viewModel.findByVegetableID(vegetableId);

        liveVegetable.observe(this.getViewLifecycleOwner(), vegetable -> {
            if (vegetable != null) {
                setDetails(vegetable);
                setDeleteConfirmation(vegetable);
                setEditListener();
            }
        });

        return binding.getRoot();
    }
}