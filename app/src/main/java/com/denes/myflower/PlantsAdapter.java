package com.denes.myflower;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView plant_name;
        public TextView statement;
        public ImageView background_img;
        public CardView card_view;


        public ViewHolder(View view) {
            super(view);

            card_view = view.findViewById(R.id.card_view1);
            plant_name = view.findViewById(R.id.title1);
            statement = view.findViewById(R.id.comment1);
            background_img = view.findViewById(R.id.img_background);

        }
    }

    List<Plants> plant_list;
    CustomItemClickListener listener;
    public PlantsAdapter(List<Plants> plant_list, CustomItemClickListener listener) {

        this.plant_list = plant_list;
        this.listener = listener;
    }


    @Override
    public PlantsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout1, parent, false);
        final ViewHolder view_holder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, view_holder.getPosition());

            }
        });

        return view_holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.plant_name.setText(plant_list.get(position).getName());
        holder.statement.setText(plant_list.get(position).getStatement());
        holder.background_img.setImageResource(plant_list.get(position).getPhoto_id());

    }

    @Override
    public int getItemCount() {
        return plant_list.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}