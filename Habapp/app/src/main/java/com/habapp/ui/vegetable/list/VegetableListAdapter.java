package com.habapp.ui.vegetable.list;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.habapp.models.Vegetable;

public class VegetableListAdapter extends ListAdapter<Vegetable, VegetableViewHolder> {

    private int actionId;

    public VegetableListAdapter(@NonNull DiffUtil.ItemCallback<Vegetable> diffCallback, int actionId) {
        super(diffCallback);
        this.actionId = actionId;
    }

    @NonNull
    @Override
    public VegetableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return VegetableViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(VegetableViewHolder holder, int position) {
        Vegetable current = getItem(position);
        holder.bind(current.getName(), current.getVegetableId(), this.actionId);
    }

    public static class VegetableDiff extends DiffUtil.ItemCallback<Vegetable> {

        @Override
        public boolean areItemsTheSame(@NonNull Vegetable oldItem, @NonNull Vegetable newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Vegetable oldItem, @NonNull Vegetable newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}

