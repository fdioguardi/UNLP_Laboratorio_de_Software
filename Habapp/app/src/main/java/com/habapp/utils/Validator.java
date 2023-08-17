package com.habapp.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Validator {

    public static boolean isEmpty(EditText editText, String message) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError(message);
            return true;
        }
        return false;
    }

    public static boolean isDateValid(TextView textView, String isEmptyMessage, String isInvalidDateMessage) {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(textView.getText().toString());
        }
        catch (ParseException e) {
            if (date == null) {
                textView.setError(isEmptyMessage);
            }
            else {
                textView.setError(isInvalidDateMessage);
            }
            return false;
        }

        return true;
    }

    public static boolean isTooLong(EditText editText, String message) {
        if (editText.getText().toString().length() > 255) {
            editText.setError(message);
            return true;
        }
        return false;
    }

    public static boolean isPositiveInt(EditText editText, String message) {
        try {
            String number = editText.getText().toString().trim();
            Integer.parseUnsignedInt(number);
        } catch (NumberFormatException e) {
            editText.setError(message);
            return false;
        }
        return true;
    }

    public static boolean isDouble(EditText editText, String message) {
        try {
            String number = editText.getText().toString().trim();
            Double.parseDouble(number);
        } catch (NumberFormatException e) {
            editText.setError(message);
            return false;
        }
        return true;
    }

    public static boolean isTextValid(EditText editText, String isEmptyMessage, String isTooLongMessage) {
        return !isEmpty(editText, isEmptyMessage) && !isTooLong(editText, isTooLongMessage);
    }

    public static boolean isDoubleValid(EditText editText, String isEmptyMessage, String isTooLongMessage, String isNotDoubleMessage) {
        return !isEmpty(editText, isEmptyMessage) && isDouble(editText, isNotDoubleMessage) && !isTooLong(editText, isTooLongMessage);
    }

    @SuppressLint("NewApi")
    public static boolean isPositiveIntValid(EditText editText, String isEmptyMessage, String isTooLongMessage, String isNotPositiveIntMessage) {
        return !isEmpty(editText, isEmptyMessage) && isPositiveInt(editText, isNotPositiveIntMessage) && !isTooLong(editText, isTooLongMessage);
    }

    public static boolean isDateOlderThanDate(TextView textView1, TextView textView2, String firstIsOlderMessage) {
        Date date1 = null, date2 = null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(textView1.getText().toString());
            date2 = new SimpleDateFormat("dd/MM/yyyy").parse(textView2.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        if (date1.after(date2)) {
            textView1.setError(firstIsOlderMessage);
            return false;
        }
        return true;
    }
}
