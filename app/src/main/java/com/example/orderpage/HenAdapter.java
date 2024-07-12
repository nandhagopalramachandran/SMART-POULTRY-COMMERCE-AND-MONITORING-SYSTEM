package com.example.orderpage;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HenAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Hen> hens;
    private Calculation shoppingCart;

    public HenAdapter(Context context, ArrayList<Hen> hens, Calculation shoppingCart) {
        this.context = context;
        this.hens = hens;
        this.shoppingCart = shoppingCart;
    }

    @Override
    public int getCount() {
        return hens.size();
    }

    @Override
    public Object getItem(int position) {
        return hens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.card_hen, null);
        }

        final Hen hen = hens.get(position);

        ImageView henImage = view.findViewById(R.id.henImage);
        TextView henName = view.findViewById(R.id.henName);
        TextView henDetails = view.findViewById(R.id.henDetails);
        ImageView decrementButton = view.findViewById(R.id.decrementButton);
        final TextView quantityEditText = view.findViewById(R.id.quantityEditText);
        ImageView incrementButton = view.findViewById(R.id.incrementButton);

        henImage.setImageResource(hen.getImageResource());
        henName.setText(hen.getName());
        henDetails.setText(String.format("%s | %s | %s", hen.getBreed(), hen.getAge(), hen.getPrice()));

        // Set initial quantity
        quantityEditText.setText(String.valueOf(hen.getQuantity()));

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = hen.getQuantity();
                if (currentQuantity > 0) {
                    hen.setQuantity(currentQuantity - 1);
                    quantityEditText.setText(String.valueOf(hen.getQuantity()));
                    // Update shopping cart if needed
                    if (currentQuantity > 1) {
                        shoppingCart.addItem(hen); // Add the item back with the new quantity
                    } else {
                        shoppingCart.removeItem(hen); // Remove the item if quantity becomes zero
                    }
                }
            }
        });

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = hen.getQuantity();
                hen.setQuantity(currentQuantity + 1);
                quantityEditText.setText(String.valueOf(hen.getQuantity()));
                shoppingCart.addItem(hen); // Update shopping cart
            }
        });

        return view;
    }
}
