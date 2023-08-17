package com.habapp.ui.vegetable.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.habapp.R;

public class VegetableViewHolder extends RecyclerView.ViewHolder {
    private final TextView vegetableItemView;

    private VegetableViewHolder(View itemView) {
        super(itemView);
        vegetableItemView = itemView.findViewById(R.id.textView);
    }

    public static VegetableViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_item, parent, false);
        return new VegetableViewHolder(view);
    }

    public void bind(String text, long id, int action) {
        vegetableItemView.setText(text);

        vegetableItemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("vegetableId", id);
            Navigation.findNavController(view).navigate(action, bundle);
        });
    }
}
