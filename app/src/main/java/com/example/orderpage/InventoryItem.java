// InventoryItem.java
package com.example.orderpage;

public class InventoryItem {
    private String itemName;
    private int count;

    public InventoryItem(String itemName, int count) {
        this.itemName = itemName;
        this.count = count;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
