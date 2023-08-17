package com.habapp.utils;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class SpanishMonth {
    private Month month;

    public SpanishMonth(Month month) {
        this.month = month;
    }

    public Month getMonth() {
        return this.month;
    }

    public String toString() {
        return month.getDisplayName(TextStyle.FULL, new Locale("es","ES"));
    }
}
