package Controllers;

import Models.Booking;
import Models.Vending.VendingMachine;
import Models.Vending.VendingMachineItem;
import Models.Vending.VendingMachineSlot;
import Storage.StorageHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendingController {
    private StorageHelper vendingStorageHelper;
    private final String ITEMS_STORE_NAME = "vending_items";
    private final String SLOTS_STORE_NAME = "vending_slots";

    public VendingController(String baseDirectory) throws IOException {
        this.vendingStorageHelper = new StorageHelper(baseDirectory, ITEMS_STORE_NAME);
        vendingStorageHelper.initializeStores(SLOTS_STORE_NAME);
    }

    public void createUpdateVendingItem(VendingMachineItem item) throws IOException {
        vendingStorageHelper.getStore(ITEMS_STORE_NAME).save(item.getName(), item.toMap());
    }

    public void createUpdateVendingSlot(VendingMachineSlot slot) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("itemName", slot.getItem().getName());
        data.put("slotNumber", slot.getSlotNumber());
        vendingStorageHelper.getStore(SLOTS_STORE_NAME).save("SLOT_" + slot.getSlotNumber(), data);
    }

    public VendingMachineItem getVendingMachineItem(String itemName) throws IOException {
        Map<String, Object> data = vendingStorageHelper.getStore(ITEMS_STORE_NAME).load(itemName);
        return data != null ? new VendingMachineItem().fromMap(data) : null;
    }

    public VendingMachineSlot getVendingSlot(int slotNumber) throws IOException {
        Map<String, Object> data = vendingStorageHelper.getStore(SLOTS_STORE_NAME).load("SLOT_" + slotNumber);
        return new VendingMachineSlot(slotNumber, getVendingMachineItem((String) data.get("itemName")));
    }

    public List<VendingMachineSlot> getAllSlots() throws IOException {
        List<VendingMachineSlot> slots = new ArrayList<>();
        List<Map<String, Object>> data = vendingStorageHelper.getStore(SLOTS_STORE_NAME).loadAll();
        for (Map<String, Object> map : data) {
            int slotNumber = ((Number) map.get("slotNumber")).intValue();
            VendingMachineItem item = getVendingMachineItem((String) map.get("itemName"));
            VendingMachineSlot slot = new VendingMachineSlot(slotNumber, item);
            slots.add(slot);
        }
        return slots;
    }

    public VendingMachine getHotelVendingMachine() throws IOException {
        return new VendingMachine(getAllSlots());
    }

    /**
     * Adds a slot to the vending machine
     * @param item The item that will go in this slot
     */
    public void addSlot(VendingMachineItem item) throws IOException {
        int slotNum = getAllSlots().size() + 1;
        VendingMachineSlot slot = new VendingMachineSlot(slotNum, item);
        createUpdateVendingSlot(slot);
    }

    public void addMoney(VendingMachine machine, double amount) {
        machine.insertMoney(amount);
    }

    public void purchaseItem(int slotNumber, VendingMachine machine) throws IOException {
        VendingMachineItem item = machine.dispenseItem(slotNumber);
        createUpdateVendingItem(item);
    }


}
