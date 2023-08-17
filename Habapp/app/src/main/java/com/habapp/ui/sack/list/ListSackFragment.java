package com.habapp.ui.sack.list;

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
import com.habapp.databinding.FragmentListSackBinding;
import com.habapp.models.Sack;
import com.habapp.models.Vegetable;
import com.habapp.ui.sack.SackViewModel;
import com.habapp.ui.sack.list.filters.GreaterAmountFilter;
import com.habapp.ui.sack.list.filters.GreaterCreationDateFilter;
import com.habapp.ui.sack.list.filters.GreaterExpirationDateFilter;
import com.habapp.ui.sack.list.filters.LesserAmountFilter;
import com.habapp.ui.sack.list.filters.LesserCreationDateFilter;
import com.habapp.ui.sack.list.filters.LesserExpirationDateFilter;
import com.habapp.ui.sack.list.filters.NameFilter;
import com.habapp.ui.vegetable.list.ListVegetableFragment;
import com.habapp.ui.vegetable.list.filters.HarvestFilter;
import com.habapp.ui.vegetable.list.filters.SowingFilter;
import com.habapp.utils.Filter;

import java.util.ArrayList;
import java.util.List;

public class ListSackFragment extends Fragment {
    private FragmentListSackBinding binding;
    private SackListAdapter adapter;
    private List<Sack> sacks;
    private Filter<Sack> filter = new NameFilter();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListSackBinding.inflate(inflater, container, false);

        this.setRecyclerView();
        this.setFab();
        return binding.getRoot();
    }

    private void setFab() {
        this.binding.fab.setOnClickListener(view -> Navigation
                .findNavController(view)
                .navigate(R.id.action_nav_sacks_to_nav_new_sack));
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
                adapter.submitList(filter.filter(newText, sacks));
                return false;
            }
        });
    }
    private void setRecyclerView() {
        adapter = new SackListAdapter(new SackListAdapter.SackDiff(), R.id.action_nav_sacks_to_fragment_detail_sack);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));

        SackViewModel viewModel = new ViewModelProvider(this).get(SackViewModel.class);
        viewModel.getAllSacks().observe(this.getViewLifecycleOwner(), sacks -> {
            this.sacks = sacks;
            this.setSearch();
            adapter.submitList(sacks);
        });
    }

    public void setSpinner() {
        Spinner spinner = binding.spinnerFilter;
        List<Filter<Sack>> filters = new ArrayList<>();
        filters.add(new NameFilter());
        filters.add(new GreaterCreationDateFilter());
        filters.add(new LesserCreationDateFilter());
        filters.add(new GreaterExpirationDateFilter());
        filters.add(new LesserExpirationDateFilter());
        filters.add(new GreaterAmountFilter());
        filters.add(new LesserAmountFilter());
        ArrayAdapter<Filter<Sack>> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, filters);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ListSackFragment.this.filter = filters.get(i);
                ListSackFragment.this.adapter.submitList(filter.filter(binding.search.getQuery().toString(), sacks));
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
