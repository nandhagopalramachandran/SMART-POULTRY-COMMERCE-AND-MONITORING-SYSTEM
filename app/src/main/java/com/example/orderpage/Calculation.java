package com.example.orderpage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Calculation {
    private static Calculation instance;
    private Map<Hen, Integer> selectedItems; // Use a Map to store unique items

    private Calculation() {
        selectedItems = new HashMap<>();
    }

    public static Calculation getInstance() {
        if (instance == null) {
            instance = new Calculation();
        }
        return instance;
    }

    public void addItem(Hen hen) {
        if (selectedItems.containsKey(hen)) {
            // If item already exists, increment its quantity
            int quantity = selectedItems.get(hen);
            selectedItems.put(hen, quantity + 1);
        } else {
            // If item doesn't exist, add it with quantity 1
            selectedItems.put(hen, 1);
        }
    }

    public void removeItem(Hen hen) {
        if (selectedItems.containsKey(hen)) {
            int quantity = selectedItems.get(hen);
            if (quantity > 1) {
                // If item has more than one quantity, decrement its quantity
                selectedItems.put(hen, quantity - 1);
            } else {
                // If item has only one quantity, remove it from selected items
                selectedItems.remove(hen);
            }
        }
    }

    public ArrayList<Hen> getSelectedItems() {
        return new ArrayList<>(selectedItems.keySet()); // Return list of unique items
    }

    public int getQuantity(Hen hen) {
        if (selectedItems.containsKey(hen)) {
            return selectedItems.get(hen);
        }
        return 0;
    }

    public int getTotalCost() {
        int totalCost = 0;
        for (Map.Entry<Hen, Integer> entry : selectedItems.entrySet()) {
            Hen hen = entry.getKey();
            int quantity = entry.getValue();
            totalCost += quantity * Integer.parseInt(hen.getPrice().replace(" ₹", ""));
        }
        return totalCost;
    }
    public void clearSelectedItems() {
        selectedItems.clear();
    }
}


