package com.habapp.ui.visit;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.habapp.MainActivity;
import com.habapp.R;
import com.habapp.databinding.FragmentDetailPlotBinding;
import com.habapp.databinding.FragmentDetailVisitBinding;
import com.habapp.databinding.FragmentEditPlotBinding;
import com.habapp.databinding.FragmentEditVisitBinding;
import com.habapp.databinding.FragmentNewVisitBinding;
import com.habapp.models.Farm;
import com.habapp.models.Plot;
import com.habapp.models.Visit;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.FarmWithPlots;
import com.habapp.models.relations.PlotWithVegetables;
import com.habapp.models.relations.VisitWithVegetables;
import com.habapp.ui.farm.FarmViewModel;
import com.habapp.ui.vegetable.VegetableViewModel;
import com.habapp.ui.vegetable.list.VegetableListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailVisitFragment extends Fragment {
    private @NonNull FragmentDetailVisitBinding binding;
    private FragmentActivity activity;
    private VisitViewModel visitViewModel;

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
        binding.data.buttonDate.setEnabled(false);
        binding.data.buttonDate.setClickable(false);
        binding.data.buttonDate.setVisibility(View.GONE);
        binding.data.editAvailableVegetables.setEnabled(false);
        binding.data.editAvailableVegetables.setClickable(false);
        binding.data.editAvailableVegetables.setVisibility(View.GONE);
        binding.data.editPersonName.setEnabled(false);
        binding.data.editDescription.setEnabled(false);
    }

    private void setRecyclerView(List<Vegetable> vegetables) {
        final VegetableListAdapter adapter = new VegetableListAdapter(new VegetableListAdapter.VegetableDiff(), R.id.action_nav_detail_visit_to_fragment_detail_vegetable);
        this.binding.recyclerView.setAdapter(adapter);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter.submitList(vegetables);
    }

    private void setDeleteConfirmation(VisitWithVegetables visitWithVegetables) {
        binding.buttonDelete.setOnClickListener(view -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            builder.setCancelable(true);
            builder.setTitle("Eliminar la visita");
            builder.setMessage("¿Estás segurx de querer borrar la visita?");
            builder.setPositiveButton("Borrar", (dialogInterface, i) -> {
                visitViewModel.delete(visitWithVegetables.getVisit());
                getActivity().onBackPressed();
            });
            builder.setNegativeButton("Cancelar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void setEditListener(int visitID) {
        binding.buttonEdit.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("visitId", visitID);
            Navigation.findNavController(view).navigate(R.id.action_fragment_detail_visit_to_fragment_edit_visit, bundle);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailVisitBinding.inflate(inflater, container, false);

        long visitPosition = getArguments().getLong("visitId");
        Log.i("TAG", Long.toString(visitPosition));
        this.activity = getActivity();
        this.disableButtons();

        this.visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);

        this.visitViewModel.getVisitWithVegetables(visitPosition).observe(getViewLifecycleOwner(), visitWithVegetables -> {
            if (visitWithVegetables != null) {
                this.setSpinner(visitWithVegetables.getVisit().getFarmId());
                this.setFormValues(visitWithVegetables.getVisit());
                this.setRecyclerView(visitWithVegetables.getAvailableVegetables());
                this.setDeleteConfirmation(visitWithVegetables);
                this.setEditListener((int) visitPosition);
            }
        });

        return binding.getRoot();
    }
}