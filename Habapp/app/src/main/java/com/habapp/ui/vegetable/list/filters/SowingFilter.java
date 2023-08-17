package com.habapp.ui.vegetable.list.filters;

import com.habapp.models.Farm;
import com.habapp.models.Vegetable;
import com.habapp.utils.Filter;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SowingFilter implements Filter<Vegetable> {

    @Override
    public List<Vegetable> filter(String searchText, List<Vegetable> vegetables) {
        return vegetables.stream().filter(vegetable -> vegetable.getSowing_month().getDisplayName(TextStyle.FULL, new Locale("es","ES")).equalsIgnoreCase(searchText)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Mes de siembra";
    }
}