package com.habapp.ui.farm.list.filters;

import com.habapp.models.Farm;
import com.habapp.utils.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LesserAreaFilter implements Filter<Farm> {
    @Override
    public List<Farm> filter(String searchText, List<Farm> farms) {
        try {
            return farms.stream().filter(farm -> farm.getArea() <= Double.parseDouble(searchText)).collect(Collectors.toList());
        }
        catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return "Área menor a";
    }
}
