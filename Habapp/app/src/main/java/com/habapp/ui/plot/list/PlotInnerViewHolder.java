package com.habapp.ui.plot.list;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.habapp.R;

public class PlotInnerViewHolder extends RecyclerView.ViewHolder {
    private final TextView plotItemView;
    private final ImageButton deleteButton;

    public PlotInnerViewHolder(View itemView, final PlotInnerAdapter.OnItemClickListener listener) {
        super(itemView);
        plotItemView = itemView.findViewById(R.id.textView);
        deleteButton = itemView.findViewById(R.id.deleteButtonInner);

        plotItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            }
        });
    }

    public void bind(String text) {
        plotItemView.setText(text);
    }
}