package com.habapp.ui.visit.list.filters;

import com.habapp.models.Farm;
import com.habapp.models.Visit;
import com.habapp.utils.Filter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class VisitorsFilter implements Filter<Visit> {
    @Override
    public List<Visit> filter(String searchText, List<Visit> visits) {
        return visits.stream().filter(visit -> visit.getVisitors().stream().anyMatch(visitor -> visitor.toLowerCase().contains(searchText.toLowerCase()))).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Técnic@";
    }
}
