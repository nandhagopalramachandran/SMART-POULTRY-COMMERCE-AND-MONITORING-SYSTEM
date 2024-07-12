package com.example.orderpage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ConfirmedOrderAdapter extends RecyclerView.Adapter<ConfirmedOrderAdapter.ConfirmedOrderViewHolder> {

    private static ArrayList<ConfirmedOrder> confirmedOrders;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private static OnItemClickListener mListener;

    public ConfirmedOrderAdapter(ArrayList<ConfirmedOrder> confirmedOrders, OnItemClickListener listener) {
        this.confirmedOrders = confirmedOrders;
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int orderId);
    }

    @NonNull
    @Override
    public ConfirmedOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmed_order_item, parent, false);
        return new ConfirmedOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmedOrderViewHolder holder, int position) {
        ConfirmedOrder currentOrder = confirmedOrders.get(position);

        // Set order details only if it's the first item of a new order ID
        if (position == 0 || currentOrder.getOrderId() != confirmedOrders.get(position - 1).getOrderId()) {
            holder.orderIdTextView.setVisibility(View.VISIBLE);
            holder.nameTextView.setVisibility(View.VISIBLE);
            holder.mobileNumberTextView.setVisibility(View.VISIBLE);
            holder.addressTextView.setVisibility(View.VISIBLE);
            holder.totalCostTextView.setVisibility(View.VISIBLE);

            holder.orderIdTextView.setText("Order ID: " + currentOrder.getOrderId());
            holder.nameTextView.setText("Name: " + currentOrder.getName());
            holder.mobileNumberTextView.setText("Mobile Number: " + currentOrder.getMobileNumber());
            holder.addressTextView.setText("Address: " + currentOrder.getAddress());
            holder.totalCostTextView.setText("Total Cost: " + currentOrder.getTotalCost());

        } else {
            // Hide order details for repeated order IDs
            holder.orderIdTextView.setVisibility(View.GONE);
            holder.nameTextView.setVisibility(View.GONE);
            holder.mobileNumberTextView.setVisibility(View.GONE);
            holder.addressTextView.setVisibility(View.GONE);
            holder.totalCostTextView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(currentOrder.getOrderId());
            }
        });

        // Highlight selected item
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.purple));
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.transparent));
        }
    }




    @Override
    public int getItemCount() {
        return confirmedOrders.size();
    }

    public static class ConfirmedOrderViewHolder extends RecyclerView.ViewHolder {
        public TextView orderIdTextView, nameTextView, mobileNumberTextView, addressTextView,
                itemNameTextView, quantityTextView, costTextView, totalCostTextView;

        public ConfirmedOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(confirmedOrders.get(position).getOrderId());
                }
            });
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            mobileNumberTextView = itemView.findViewById(R.id.mobileNumberTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            costTextView = itemView.findViewById(R.id.costTextView);
            totalCostTextView = itemView.findViewById(R.id.totalCostTextView);
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public int getPositionByOrderId(int orderId) {
        for (int i = 0; i < confirmedOrders.size(); i++) {
            if (confirmedOrders.get(i).getOrderId() == orderId) {
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }
}
