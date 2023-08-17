package com.habapp.ui.visit.list;

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
import com.habapp.databinding.FragmentListVisitBinding;
import com.habapp.models.Farm;
import com.habapp.models.Visit;
import com.habapp.ui.farm.list.ListFarmFragment;
import com.habapp.ui.farm.list.filters.AddressFilter;
import com.habapp.ui.farm.list.filters.GreaterAreaFilter;
import com.habapp.ui.farm.list.filters.LesserAreaFilter;
import com.habapp.ui.visit.VisitViewModel;
import com.habapp.ui.visit.list.filters.GreaterDateFilter;
import com.habapp.ui.visit.list.filters.LesserDateFilter;
import com.habapp.ui.visit.list.filters.NameFilter;
import com.habapp.ui.visit.list.filters.VisitorsFilter;
import com.habapp.utils.Filter;

import java.util.ArrayList;
import java.util.List;

public class ListVisitFragment extends Fragment {
    private FragmentListVisitBinding binding;
    private List<Visit> visits;
    private Filter<Visit> filter = new NameFilter();
    private VisitListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListVisitBinding.inflate(inflater, container, false);

        this.setRecyclerView();
        this.setFab();
        return binding.getRoot();
    }

    private void setFab() {
        this.binding.fab.setOnClickListener(view -> Navigation
                .findNavController(view)
                .navigate(R.id.action_nav_visits_to_nav_new_visit));
    }

    private void setRecyclerView() {
        adapter = new VisitListAdapter(new VisitListAdapter.VisitDiff(), R.id.action_nav_visits_to_fragment_detail_visit);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));

        VisitViewModel viewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        viewModel.getAllVisits().observe(this.getViewLifecycleOwner(), visits -> {
            this.visits = visits;
            this.setSearch();
            adapter.submitList(visits);
        });
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
                adapter.submitList(filter.filter(newText, visits));
                return false;
            }
        });
    }

    public void setSpinner() {
        Spinner spinner = binding.spinnerFilter;
        List<Filter<Visit>> filters = new ArrayList<>();
        filters.add(new NameFilter());
        filters.add(new GreaterDateFilter());
        filters.add(new LesserDateFilter());
        filters.add(new VisitorsFilter());
        ArrayAdapter<Filter<Visit>> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, filters);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ListVisitFragment.this.filter = filters.get(i);
                ListVisitFragment.this.adapter.submitList(filter.filter(binding.search.getQuery().toString(), visits));
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