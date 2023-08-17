package com.habapp.ui.plot.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.habapp.MainActivity;
import com.habapp.R;
import com.habapp.models.Plot;

public class PlotInnerAdapter extends ListAdapter<Plot, PlotInnerViewHolder> {

    private int actionId;
    private MainActivity mainActivity;
    private OnItemClickListener mListener;

    public PlotInnerAdapter(@NonNull DiffUtil.ItemCallback<Plot> diffCallback) {
        super(diffCallback);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public PlotInnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inner_list_item, parent, false);
        return new PlotInnerViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(PlotInnerViewHolder holder, int position) {
        Plot current = getItem(position);
        holder.bind(current.getName());
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
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

