package com.habapp.ui.farm.list;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.habapp.models.Farm;

public class FarmListAdapter extends ListAdapter<Farm, FarmViewHolder> {
    public FarmListAdapter(@NonNull DiffUtil.ItemCallback<Farm> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public FarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return FarmViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(FarmViewHolder holder, int position) {
        Farm current = getItem(position);
        holder.bind(current.getName(), current.getFarmId());
    }

    static class FarmDiff extends DiffUtil.ItemCallback<Farm> {

        @Override
        public boolean areItemsTheSame(@NonNull Farm oldItem, @NonNull Farm newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Farm oldItem, @NonNull Farm newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
