package com.denes.myflower;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FlowersAdapter extends RecyclerView.Adapter<FlowersAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView flower_name;
        public TextView statement;
        public ImageView background_img;
        public CardView card_view;


        public ViewHolder(View view) {
            super(view);

            card_view = view.findViewById(R.id.card_view);
            flower_name = view.findViewById(R.id.title);
            statement = view.findViewById(R.id.comment);
            background_img = view.findViewById(R.id.img_background);

        }
    }

    List<Flowers> list_flowers;
    CustomItemClickListener listener;
    public FlowersAdapter(List<Flowers> list_flowers, CustomItemClickListener listener) {

        this.list_flowers = list_flowers;
        this.listener = listener;
    }


    @Override
    public FlowersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout, parent, false);
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

        holder.flower_name.setText(list_flowers.get(position).getName());
        holder.statement.setText(list_flowers.get(position).getStatement());
        holder.background_img.setImageResource(list_flowers.get(position).getPhoto_id());

    }

    @Override
    public int getItemCount() {
        return list_flowers.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
