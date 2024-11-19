package Models.Vending;

import Models.Mappable;

public class VendingMachineItem extends Mappable<VendingMachineItem> {

    private String name; // Name of the item (e.g., "Chips", "Soda")
    private double price; // Price of the item
    private int quantity; // Number of items available in stock

    public VendingMachineItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public VendingMachineItem() {
        super();
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // Update quantity when an item is sold
    public boolean dispense() {
        if (quantity > 0) {
            quantity--;
            return true;
        }
        return false; // Out of stock
    }

    // Restock items
    public void restock(int amount) {
        quantity += amount;
    }

    @Override
    public String toString() {
        return name + " ($" + price + "), Quantity: " + quantity;
    }
}

