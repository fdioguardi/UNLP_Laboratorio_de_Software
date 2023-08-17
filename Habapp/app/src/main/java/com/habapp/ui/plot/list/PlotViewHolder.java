package com.habapp.ui.plot.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.habapp.R;

public class PlotViewHolder extends RecyclerView.ViewHolder {
    private final TextView plotItemView;

    private PlotViewHolder(View itemView) {
        super(itemView);
        plotItemView = itemView.findViewById(R.id.textView);
    }

    public static PlotViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_item, parent, false);
        return new PlotViewHolder(view);
    }

    public void bind(String text, int action, long plotID) {
        plotItemView.setText(text);

        Bundle bundle = new Bundle();
        bundle.putLong("plotId", plotID);

        plotItemView.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(action, bundle);
        });
    }
}