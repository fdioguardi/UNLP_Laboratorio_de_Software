package com.habapp.ui.farm.list.filters;

import com.habapp.models.Farm;
import com.habapp.utils.Filter;

import java.util.List;
import java.util.stream.Collectors;

public class AddressFilter implements Filter<Farm> {
    @Override
    public List<Farm> filter(String searchText, List<Farm> farms) {
        return farms.stream().filter(farm -> farm.getAddress().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Dirección";
    }
}
