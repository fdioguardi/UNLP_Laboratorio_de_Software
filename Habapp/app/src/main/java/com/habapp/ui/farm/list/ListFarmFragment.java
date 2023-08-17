package com.habapp.ui.farm.list;

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

import com.habapp.MainActivity;
import com.habapp.R;
import com.habapp.databinding.FragmentListFarmBinding;
import com.habapp.models.Farm;
import com.habapp.models.Vegetable;
import com.habapp.ui.farm.FarmViewModel;
import com.habapp.ui.farm.list.filters.AddressFilter;
import com.habapp.ui.farm.list.filters.GreaterAreaFilter;
import com.habapp.ui.farm.list.filters.LesserAreaFilter;
import com.habapp.ui.farm.list.filters.NameFilter;
import com.habapp.ui.vegetable.list.ListVegetableFragment;
import com.habapp.ui.vegetable.list.filters.HarvestFilter;
import com.habapp.ui.vegetable.list.filters.SowingFilter;
import com.habapp.utils.Filter;

import java.util.ArrayList;
import java.util.List;

public class ListFarmFragment extends Fragment {

    private FragmentListFarmBinding binding;
    private List<Farm> farms;
    final FarmListAdapter adapter = new FarmListAdapter(new FarmListAdapter.FarmDiff());

    private Filter<Farm> filter = new NameFilter();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListFarmBinding.inflate(inflater, container, false);

        this.setRecyclerView();
        this.setFab();

        MainActivity mainActivity = (MainActivity) this.getActivity();
        mainActivity.clearPlots();

        return binding.getRoot();
    }

    private void setSearch() {
        this.setSpinner();
        this.binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.submitList(filter.filter(newText, farms));
                return false;
            }
        });
    }

    private void setFab() {
        this.binding.fab.setOnClickListener(view -> Navigation
                .findNavController(view)
                .navigate(R.id.action_nav_farm_to_nav_new_farm));
    }

    private void setRecyclerView() {
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));

        FarmViewModel viewModel = new ViewModelProvider(this).get(FarmViewModel.class);
        viewModel.getAllFarms().observe(this.getViewLifecycleOwner(), farms -> {
            this.farms = farms;
            this.setSearch();
            adapter.submitList(farms);
        });
    }

    public void setSpinner() {
        Spinner spinner = binding.spinnerFilter;
        List<Filter<Farm>> filters = new ArrayList<>();
        filters.add(new NameFilter());
        filters.add(new AddressFilter());
        filters.add(new GreaterAreaFilter());
        filters.add(new LesserAreaFilter());
        ArrayAdapter<Filter<Farm>> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, filters);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ListFarmFragment.this.filter = filters.get(i);
                ListFarmFragment.this.adapter.submitList(filter.filter(binding.search.getQuery().toString(), farms));
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