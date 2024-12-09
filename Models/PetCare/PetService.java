package Models.PetCare;

import Models.Mappable;

public class PetService extends Mappable<PetService> {
    private String serviceName;
    private double servicePrice;

    public PetService() {}

    public PetService(String serviceName, double servicePrice) {
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    @Override
    public String toString() {
        return "Service Name: " + serviceName + ", Service Price: $" + servicePrice;
    }
}
