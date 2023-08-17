package com.habapp.ui.visit.list;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.habapp.models.Visit;

public class VisitListAdapter extends ListAdapter<Visit, VisitViewHolder> {

    private int actionId;

    public VisitListAdapter(@NonNull DiffUtil.ItemCallback<Visit> diffCallback, int actionId) {
        super(diffCallback);
        this.actionId = actionId;
    }

    @NonNull
    @Override
    public VisitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return VisitViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(VisitViewHolder holder, int position) {
        Visit current = getItem(position);
        holder.bind(current.getDescription(), current.getVisitId(), this.actionId);
    }

    public static class VisitDiff extends DiffUtil.ItemCallback<Visit> {

        @Override
        public boolean areItemsTheSame(@NonNull Visit oldItem, @NonNull Visit newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Visit oldItem, @NonNull Visit newItem) {
            return oldItem.getVisitId() == newItem.getVisitId();
        }
    }
}

