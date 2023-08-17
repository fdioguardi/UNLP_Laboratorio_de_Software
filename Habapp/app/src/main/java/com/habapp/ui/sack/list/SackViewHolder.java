package com.habapp.ui.sack.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.habapp.R;

public class SackViewHolder extends RecyclerView.ViewHolder {
    private final TextView sackItemView;

    private SackViewHolder(View itemView) {
        super(itemView);
        sackItemView = itemView.findViewById(R.id.textView);
    }

    public static SackViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_item, parent, false);
        return new SackViewHolder(view);
    }

    public void bind(String text, long id, int action) {
        sackItemView.setText(text);

        sackItemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("sackId", id);
            Navigation.findNavController(view).navigate(action, bundle);
        });
    }
}
