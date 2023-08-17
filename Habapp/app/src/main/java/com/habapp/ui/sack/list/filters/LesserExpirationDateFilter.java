package com.habapp.ui.sack.list.filters;

import com.habapp.models.Sack;
import com.habapp.utils.Filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LesserExpirationDateFilter implements Filter<Sack> {
    @Override
    public List<Sack> filter(String searchText, List<Sack> sacks) {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(searchText);
        } catch (ParseException e) {
            return new ArrayList<Sack>();
        }

        Date finalDate = date;
        return sacks.stream().filter(sack -> sack.getExpirationDate().before(finalDate)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Fecha de expiración menor a";
    }
}
