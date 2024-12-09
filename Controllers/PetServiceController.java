package Controllers;

import Models.PetCare.Pet;
import Models.PetCare.PetService;
import Models.PetCare.PetServiceBooking;
import Models.PetCare.PetServiceDesk;
import Storage.StorageHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PetServiceController {

    private StorageHelper storage;
    private final String PET_SERVICES_STORAGE = "pet_services";
    private final String PET_SERVICE_BOOKING_STORAGE = "pet_service_booking";
    private final String PET_STORAGE = "pets";

    private PetServiceDesk petServiceDesk;

    public PetServiceController(String baseDirectory) throws IOException {
        storage = new StorageHelper(baseDirectory, PET_SERVICES_STORAGE);
        storage.initializeStores(PET_SERVICE_BOOKING_STORAGE);
        storage.initializeStores(PET_STORAGE);
        petServiceDesk = new PetServiceDesk(getAllPetServices());
    }

    public void createPetService(PetService petService) throws IOException {
        storage.getStore(PET_SERVICES_STORAGE).save(petService.getServiceName(), petService.toMap());
    }

    public PetService getPetService(String name) throws IOException {
        Map<String, Object> data = storage.getStore(PET_SERVICES_STORAGE).load(name);
        return data != null ? new PetService().fromMap(data) : null;
    }

    public List<PetService> getAllPetServices() throws IOException {
        List<PetService> services = new ArrayList<>();
        List<Map<String, Object>> data = storage.getStore(PET_SERVICES_STORAGE).loadAll();
        for(Map<String, Object> service : data) {
            services.add(new PetService().fromMap(service));
        }
        return services;
    }

    public void deletePetService(String name) throws IOException {
        storage.getStore(PET_SERVICES_STORAGE).delete(name);
    }

    public void createPetServiceBooking(PetServiceBooking booking) throws IOException {
        storage.getStore(PET_SERVICE_BOOKING_STORAGE).save(String.valueOf(booking.getId()), booking.toMap());
    }

    public void deletePetServiceBooking(int id) throws IOException {
        storage.getStore(PET_SERVICE_BOOKING_STORAGE).delete(String.valueOf(id));
    }

    public PetServiceBooking getPetServiceBooking(int id) throws IOException {
        Map<String, Object> data = storage.getStore(PET_SERVICE_BOOKING_STORAGE).load(String.valueOf(id));
        return data != null ? new PetServiceBooking().fromMap(data) : null;
    }

    public List<PetServiceBooking> getAllPetServiceBookings() throws IOException {
        List<Map<String, Object>> data = storage.getStore(PET_SERVICE_BOOKING_STORAGE).loadAll();
        List<PetServiceBooking> bookings = new ArrayList<>();
        for(Map<String, Object> service : data) {
            bookings.add(new PetServiceBooking().fromMap(service));
        }
        return bookings;
    }

    public void createPet(Pet pet) throws IOException {
        storage.getStore(PET_STORAGE).save(pet.getName(), pet.toMap());
    }

    public Pet getPet(String petName) throws IOException {
        Map<String, Object> data = storage.getStore(PET_STORAGE).load(petName);
        return data != null ? new Pet().fromMap(data) : null;
    }

    public void deletePet(String petName) throws IOException {
        storage.getStore(PET_STORAGE).delete(petName);
    }

    public List<Pet> getAllPets() throws IOException {
        List<Map<String, Object>> data = storage.getStore(PET_STORAGE).loadAll();
        List<Pet> pets = new ArrayList<>();
        for(Map<String, Object> pet : data) {
            pets.add(new Pet().fromMap(pet));
        }
        return pets;
    }

    public void addPetService() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Pet Service Name: ");
        String petServiceName = scanner.nextLine();
        System.out.print("Service Price: ");
        double servicePrice = scanner.nextDouble();

        PetService petService = new PetService(petServiceName, servicePrice);
        createPetService(petService);
        System.out.println("\nPet Service Added\n");
    }

    public void removePetService() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Pet Service Name: ");
        String petServiceName = scanner.nextLine();
        if(getPetService(petServiceName) == null) {
            System.out.println("\nERROR: Pet service not found.\n");
            return;
        }

        deletePetService(petServiceName);
        System.out.println("\nPet Service Removed\n");
    }

    public void displayAllPetServices() throws IOException {
        List<PetService> petServiceList = getAllPetServices();
        if(petServiceList.isEmpty()) {
            System.out.println("No pet services.");
            return;
        }

        System.out.println();

        for(PetService petService : petServiceList) {
            System.out.println(petService);
        }
    }

    public void displayAllBookings() throws IOException {
        List<PetServiceBooking> petServiceBookingList = getAllPetServiceBookings();
        if(petServiceBookingList.isEmpty()) {
            System.out.println("No booking services.");
            return;
        }
        System.out.println();

        for(PetServiceBooking booking : petServiceBookingList) {
            System.out.println(booking);
        }
    }

    public PetServiceBooking AddPetBooking(Pet pet, PetService service) throws IOException {
        createPet(pet);
        PetServiceBooking booking = new PetServiceBooking(pet.getName(), getAllPetServiceBookings().size() + 1,
                service.getServiceName());
        createPetServiceBooking(booking);
        return booking;
    }

    public void CancelPetServiceBooking() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter Pet Service Booking ID to cancel booking: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        PetServiceBooking booking = getPetServiceBooking(id);
        if(booking == null) {
            System.out.println("\nERROR: Booking not found.\n");
            return;
        }

        System.out.print("Are you sure you want to cancel " + booking + "? (y / n) ");
        String choice = scanner.nextLine();

        if(choice.equalsIgnoreCase("y")) {
            deletePet(booking.getPetName());
            deletePetServiceBooking(booking.getId());
            System.out.println("\nPet Service Booking Canceled\n");
        }
    }

    public void DisplayAllPets() throws IOException {
        List<Pet> pets = getAllPets();

        if(pets.isEmpty()) {
            System.out.println("No pets.");
            return;
        }
        System.out.println();

        for(Pet pet : pets) {
            System.out.println(pet);
        }
    }

}
