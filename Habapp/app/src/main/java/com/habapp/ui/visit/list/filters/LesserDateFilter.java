package com.habapp.ui.visit.list.filters;

import com.habapp.models.Visit;
import com.habapp.utils.Filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LesserDateFilter implements Filter<Visit> {
    @Override
    public List<Visit> filter(String searchText, List<Visit> visits) {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(searchText);
        } catch (ParseException e) {
            return new ArrayList<Visit>();
        }

        Date finalDate = date;
        return visits.stream().filter(visit -> visit.getDate().before(finalDate)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Fecha menor a";
    }
}
