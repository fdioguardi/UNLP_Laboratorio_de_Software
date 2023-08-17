package com.habapp.ui.sack.list.filters;

import com.habapp.models.Sack;
import com.habapp.utils.Filter;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class NameFilter implements Filter<Sack> {

    @Override
    public List<Sack> filter(String searchText, List<Sack> sacks) {
        return sacks.stream().filter(sack -> sack.getDescription().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Nombre";
    }
}
