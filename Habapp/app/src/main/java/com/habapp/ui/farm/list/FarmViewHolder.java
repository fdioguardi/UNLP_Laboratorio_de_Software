package com.habapp.ui.farm.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.habapp.R;

public class FarmViewHolder extends RecyclerView.ViewHolder {
    private final TextView farmItemView;
    private long farmId;

    private FarmViewHolder(View itemView) {
        super(itemView);
        farmItemView = itemView.findViewById(R.id.textView);
    }

    public static FarmViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_item, parent, false);
        return new FarmViewHolder(view);
    }

    public void bind(String text, long id) {
        farmId = id;
        farmItemView.setText(text);

        farmItemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("farmId", farmId);
            Navigation.findNavController(view).navigate(R.id.action_nav_farm_to_nav_detail_farm, bundle);
        });
    }
}
