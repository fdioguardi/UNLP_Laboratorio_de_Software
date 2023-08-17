package com.habapp.ui.visit.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.habapp.R;

public class VisitViewHolder extends RecyclerView.ViewHolder {
    private final TextView visitItemView;

    private VisitViewHolder(View itemView) {
        super(itemView);
        visitItemView = itemView.findViewById(R.id.textView);
    }

    public static VisitViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_item, parent, false);
        return new VisitViewHolder(view);
    }

    public void bind(String text, long id, int action) {
        visitItemView.setText(text);

        visitItemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("visitId", id);
            Navigation.findNavController(view).navigate(action, bundle);
        });
    }
}
