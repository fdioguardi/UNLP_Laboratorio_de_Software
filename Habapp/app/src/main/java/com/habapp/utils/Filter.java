package com.habapp.utils;

import java.util.List;

public interface Filter<T> {
    public List<T> filter(String searchText, List<T> items);
    public String toString();
}
