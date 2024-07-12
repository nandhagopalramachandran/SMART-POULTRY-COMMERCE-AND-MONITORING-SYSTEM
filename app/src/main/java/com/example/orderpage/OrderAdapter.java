package com.example.orderpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<Order1> {

    private Context context;
    private ArrayList<Order1> orders;

    public OrderAdapter(Context context, ArrayList<Order1> orders) {
        super(context, 0, orders);
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.track_address_item, parent, false);
        }

        Order1 currentOrder = orders.get(position);

        TextView orderIdTextView = listItemView.findViewById(R.id.orderIdTextView);
        orderIdTextView.setText("Order ID: " + currentOrder.getOrderId());

        TextView nameTextView = listItemView.findViewById(R.id.nameTextView);
        nameTextView.setText("Name: " + currentOrder.getName());

        return listItemView;
    }
}
