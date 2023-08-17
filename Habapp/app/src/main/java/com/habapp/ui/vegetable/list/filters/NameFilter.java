package com.habapp.ui.vegetable.list.filters;

import com.habapp.models.Farm;
import com.habapp.models.Vegetable;
import com.habapp.utils.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NameFilter implements Filter<Vegetable> {

    @Override
    public List<Vegetable> filter(String searchText, List<Vegetable> vegetables) {
        return vegetables.stream().filter(vegetable -> vegetable.getName().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Nombre";
    }
}
