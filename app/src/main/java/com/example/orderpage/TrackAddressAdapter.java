package com.example.orderpage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrackAddressAdapter extends RecyclerView.Adapter<TrackAddressAdapter.TrackAddressViewHolder> {

    private ArrayList<Order> orderList;
    private OnItemClickListener mListener;

    public TrackAddressAdapter(ArrayList<Order> orderList, OnItemClickListener listener) {
        this.orderList = orderList;
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int orderId);
    }

    @NonNull
    @Override
    public TrackAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_address_item, parent, false);
        return new TrackAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackAddressViewHolder holder, int position) {
        Order currentOrder = orderList.get(position);

        holder.orderIdTextView.setText("Order ID: " + currentOrder.getOrderId());
        holder.nameTextView.setText("Name: " + currentOrder.getName());

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(currentOrder.getOrderId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class TrackAddressViewHolder extends RecyclerView.ViewHolder {
        public TextView orderIdTextView, nameTextView;

        public TrackAddressViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
