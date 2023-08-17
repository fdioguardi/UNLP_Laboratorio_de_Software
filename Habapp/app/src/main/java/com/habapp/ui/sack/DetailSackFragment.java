package com.habapp.ui.sack;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.habapp.R;
import com.habapp.databinding.FragmentDetailSackBinding;
import com.habapp.models.Farm;
import com.habapp.models.Sack;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.SackWithVegetables;
import com.habapp.ui.farm.FarmViewModel;
import com.habapp.ui.vegetable.list.VegetableListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailSackFragment extends Fragment {
    private @NonNull FragmentDetailSackBinding binding;
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

    private void setSpinner(Long farmID) {
        FarmViewModel farmViewModel = new ViewModelProvider(this).get(FarmViewModel.class);
        farmViewModel.findByFarmId(farmID).observe(getViewLifecycleOwner(), farm -> {
            Spinner spinner = binding.spinnerFarmName;
            List<Farm> farmList = new ArrayList<>();
            farmList.add(farm);
            ArrayAdapter<Farm> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, farmList);
            spinner.setAdapter(adapter);
        });
    }

    private void disableButtons() {
        binding.spinnerFarmName.setEnabled(false);
        binding.data.buttonCreationDate.setEnabled(false);
        binding.data.buttonExpirationDate.setEnabled(false);
        binding.data.buttonCreationDate.setClickable(false);
        binding.data.buttonExpirationDate.setClickable(false);
        binding.data.buttonCreationDate.setVisibility(View.GONE);
        binding.data.buttonExpirationDate.setVisibility(View.GONE);
        binding.data.editVegetables.setEnabled(false);
        binding.data.editVegetables.setClickable(false);
        binding.data.editVegetables.setVisibility(View.GONE);
        binding.data.editAmount.setEnabled(false);
        binding.data.editDescription.setEnabled(false);
    }

    private void setRecyclerView(List<Vegetable> vegetables) {
        final VegetableListAdapter adapter = new VegetableListAdapter(new VegetableListAdapter.VegetableDiff(), R.id.action_nav_detail_sack_to_fragment_detail_vegetable);
        this.binding.recyclerView.setAdapter(adapter);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter.submitList(vegetables);
    }

    private void setDeleteConfirmation(SackWithVegetables sackWithVegetables) {
        binding.buttonDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(true);
            builder.setTitle("Eliminar el bolsón");
            builder.setMessage("¿Estás segurx de querer borrar el bolsón?");
            builder.setPositiveButton("Borrar", (dialogInterface, i) -> {
                sackViewModel.delete(sackWithVegetables.getSack());
                getActivity().onBackPressed();
            });
            builder.setNegativeButton("Cancelar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void setEditListener(int sackId) {
        binding.buttonEdit.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("sackId", sackId);
            Navigation.findNavController(view).navigate(R.id.action_fragment_detail_sack_to_fragment_edit_sack, bundle);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailSackBinding.inflate(inflater, container, false);

        long sackPosition = getArguments().getLong("sackId");
        Log.i("TAG", Long.toString(sackPosition));
        this.disableButtons();

        this.sackViewModel = new ViewModelProvider(this).get(SackViewModel.class);

        this.sackViewModel.getSackWithVegetables(sackPosition).observe(getViewLifecycleOwner(), sackWithVegetables -> {
            if (sackWithVegetables != null) {
                this.setSpinner(sackWithVegetables.getSack().getFarmId());
                this.setFormValues(sackWithVegetables.getSack());
                this.setRecyclerView(sackWithVegetables.getVegetables());
                this.setDeleteConfirmation(sackWithVegetables);
                this.setEditListener((int) sackPosition);
            }
        });

        return binding.getRoot();
    }
}