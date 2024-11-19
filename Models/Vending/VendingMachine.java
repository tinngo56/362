package Models.Vending;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

public class VendingMachine {

    private Map<Integer, VendingMachineSlot> slots; // Slot number to slot mapping
    private double balance; // Balance inserted by the user

    public VendingMachine() {
        this.slots = new HashMap<>();
        this.balance = 0.0;
    }

    public VendingMachine(List<VendingMachineSlot> slots) {
        this.slots = new HashMap<>();
        for (VendingMachineSlot slot : slots) {
            this.slots.put(slot.getSlotNumber(), slot);
        }
        this.balance = 0.0;
    }

    /**
     * Adds a slot.
     * @param slot slot to add
     */
    public void addSlot(VendingMachineSlot slot) {
        slots.put(slot.getSlotNumber(), slot);
    }

    /**
     * Displays all slots
     */
    public void displaySlots() {
        for (VendingMachineSlot slot : slots.values()) {
            System.out.println(slot.toString());
        }
    }

    /**
     * Inserts money into machine. Maximum $50 can be inserted at once.
     * @param amount total amount to insert.
     */
    public void insertMoney(double amount) {
        if (amount > 0 && amount < 50) {
            balance += amount;
            System.out.println("Inserted: $" + amount + ". Current balance: $" + balance);
        } else {
            System.out.println("ERROR: Invalid amount inserted.\n");
        }
    }

    // Dispense an item by slot number
    public VendingMachineItem dispenseItem(int slotNumber) {
        VendingMachineSlot slot = slots.get(slotNumber);
        if (slot == null) {
            System.out.println("ERROR: Invalid slot number.\n");
            return null;
        }

        if (slot.isEmpty()) {
            System.out.println("ERROR: Slot " + slotNumber + " is empty.\n");
            return null;
        }

        VendingMachineItem item = slot.getItem();
        if (balance >= item.getPrice()) {
            if (slot.dispenseItem()) {
                balance -= item.getPrice();
                System.out.println("Dispensed: " + item.getName() + ". Remaining balance: $" + balance);
            } else {
                System.out.println("ERROR: Failed to dispense item.\n");
            }
        } else {
            System.out.println("ERROR: Insufficient balance. Please insert more money.\n");
        }
        return item;
    }

    // Return remaining balance to the user
    public void returnBalance() {
        System.out.println("Returning balance: $" + balance);
        balance = 0.0;
    }

    public double getBalance() {
        return balance;
    }

    public int getNumSlots() {
        return slots.size();
    }
}


