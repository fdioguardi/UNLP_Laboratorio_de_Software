package com.habapp.ui.sack.list;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.habapp.models.Sack;

public class SackListAdapter extends ListAdapter<Sack, SackViewHolder> {

    private final int actionId;

    public SackListAdapter(@NonNull DiffUtil.ItemCallback<Sack> diffCallback, int actionId) {
        super(diffCallback);
        this.actionId = actionId;
    }

    @NonNull
    @Override
    public SackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SackViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(SackViewHolder holder, int position) {
        Sack current = getItem(position);
        holder.bind(current.getDescription(), current.getSackId(), this.actionId);
    }

    public static class SackDiff extends DiffUtil.ItemCallback<Sack> {

        @Override
        public boolean areItemsTheSame(@NonNull Sack oldItem, @NonNull Sack newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Sack oldItem, @NonNull Sack newItem) {
            return oldItem.getSackId() == newItem.getSackId();
        }
    }
}

