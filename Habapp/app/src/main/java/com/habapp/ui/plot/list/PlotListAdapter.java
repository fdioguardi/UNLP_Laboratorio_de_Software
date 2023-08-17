package com.habapp.ui.plot.list;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.habapp.models.Plot;

public class PlotListAdapter extends ListAdapter<Plot, PlotViewHolder> {

    private final int actionId;

    public PlotListAdapter(@NonNull DiffUtil.ItemCallback<Plot> diffCallback, int actionId) {
        super(diffCallback);
        this.actionId = actionId;
    }

    @NonNull
    @Override
    public PlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlotViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(PlotViewHolder holder, int position) {
        Plot current = getItem(position);
        holder.bind(current.getName(), this.actionId, current.getPlotId());
    }

    public static class PlotDiff extends DiffUtil.ItemCallback<Plot> {

        @Override
        public boolean areItemsTheSame(@NonNull Plot oldItem, @NonNull Plot newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Plot oldItem, @NonNull Plot newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
