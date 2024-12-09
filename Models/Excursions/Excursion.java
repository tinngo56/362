package Models.Excursions;

import Models.Mappable;

import java.util.Objects;

public class Excursion extends Mappable<Excursion> {
    private String name;
    private double price;
    private String description;
    private int availableSlots;

    public Excursion() {}

    public Excursion(String name, double price, String description, int availableSlots) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.availableSlots = availableSlots;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public boolean isAvailable() {
        return availableSlots > 0;
    }

    public void bookSlot() {
        if (availableSlots > 0) {
            availableSlots--;
        } else {
            throw new IllegalStateException("No slots available!");
        }
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n Price: $" + price + ", Description: " + description + ", Slots: " + availableSlots + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Excursion excursion = (Excursion) o;
        return Objects.equals(name, excursion.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
