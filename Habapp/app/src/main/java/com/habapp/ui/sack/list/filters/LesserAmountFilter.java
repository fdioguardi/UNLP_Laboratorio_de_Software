package com.habapp.ui.sack.list.filters;

import com.habapp.models.Sack;
import com.habapp.utils.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LesserAmountFilter implements Filter<Sack> {
    @Override
    public List<Sack> filter(String searchText, List<Sack> sacks) {
        try {
            return sacks.stream().filter(sack -> sack.getAmount() <= Double.parseDouble(searchText)).collect(Collectors.toList());
        }
        catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return "Cantidad de unidades menor a";
    }
}
