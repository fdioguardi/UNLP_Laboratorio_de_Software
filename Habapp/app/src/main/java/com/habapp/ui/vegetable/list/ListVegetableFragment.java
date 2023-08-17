package com.habapp.ui.vegetable.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.habapp.R;
import com.habapp.databinding.FragmentListVegetableBinding;
import com.habapp.models.Vegetable;
import com.habapp.ui.vegetable.VegetableViewModel;
import com.habapp.ui.vegetable.list.filters.HarvestFilter;
import com.habapp.ui.vegetable.list.filters.NameFilter;
import com.habapp.ui.vegetable.list.filters.SowingFilter;
import com.habapp.utils.Filter;

import java.util.ArrayList;
import java.util.List;

public class ListVegetableFragment extends Fragment {

    private FragmentListVegetableBinding binding;
    private VegetableListAdapter adapter;
    private List<Vegetable> vegetables;
    private Filter<Vegetable> filter = new NameFilter();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListVegetableBinding.inflate(inflater, container, false);

        this.setRecyclerView();
        this.setFab();
        return binding.getRoot();
    }

    private void setFab() {
        this.binding.fab.setOnClickListener(view -> Navigation
                .findNavController(view)
                .navigate(R.id.action_nav_vegetables_to_nav_new_vegetable));
    }

    private void setRecyclerView() {
        adapter = new VegetableListAdapter(new VegetableListAdapter.VegetableDiff(), R.id.action_nav_vegetables_to_fragment_detail_vegetable);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));

        VegetableViewModel viewModel = new ViewModelProvider(this).get(VegetableViewModel.class);
        viewModel.getAllVegetables().observe(this.getViewLifecycleOwner(), vegetables -> {
            this.vegetables = vegetables;
            this.setSearch();
            adapter.submitList(vegetables);
        });
    }

    private void setSearch() {
        this.setSpinner();
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.submitList(filter.filter(newText, vegetables));
                return false;
            }
        });
    }

    public void setSpinner() {
        Spinner spinner = binding.spinnerFilter;
        List<Filter<Vegetable>> filters = new ArrayList<>();
        filters.add(new NameFilter());
        filters.add(new HarvestFilter());
        filters.add(new SowingFilter());
        ArrayAdapter<Filter<Vegetable>> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, filters);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ListVegetableFragment.this.filter = filters.get(i);
                ListVegetableFragment.this.adapter.submitList(filter.filter(binding.search.getQuery().toString(), vegetables));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}