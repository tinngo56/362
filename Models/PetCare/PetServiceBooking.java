package Models.PetCare;

import Models.Mappable;

public class PetServiceBooking extends Mappable<PetServiceBooking> {
    private String petName;
    private String petServiceName;
    private int id;

    public PetServiceBooking() {}

    public PetServiceBooking(String petName, int id, String petServiceName) {
        this.petName = petName;
        this.id = id;
        this.petServiceName = petServiceName;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPetServiceName() {
        return petServiceName;
    }

    @Override
    public String toString() {
        return "Booking ID: " + id + ", Service Name: " + petServiceName + ", Pet Name: " + petName;
    }

}
