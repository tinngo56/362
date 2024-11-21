package Models.Vending;

import java.util.Stack;

public class VendingMachineSlot {

    private int slotNumber; // Slot ID or position
    private VendingMachineItem item; // The item in the slot

    public VendingMachineSlot(int slotNumber, VendingMachineItem item) {
        this.slotNumber = slotNumber;
        this.item = item;
    }

    // Getters
    public int getSlotNumber() {
        return slotNumber;
    }

    public VendingMachineItem getItem() {
        return item;
    }

    // Dispense an item
    public boolean dispenseItem() {
        if (item != null) {
            return item.dispense();
        }
        return false; // No item in the slot
    }

    // Check if slot is empty
    public boolean isEmpty() {
        return item == null || item.getQuantity() == 0;
    }

    // Restock the slot
    public void restockItem(int amount) {
        if (item != null) {
            item.restock(amount);
        }
    }

    @Override
    public String toString() {
        return "Slot " + slotNumber + ": " + (item != null ? item.toString() : "Empty");
    }
}

