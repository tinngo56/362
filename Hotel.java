public class Hotel {
    private int id;
    private String name;
    private String location;
    private int roomCount;
    private double propertyTax;
    private double size;
    private double rating;

    public Hotel(int id, String name, String location, int roomCount, double propertyTax, double size, double rating) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.roomCount = roomCount;
        this.propertyTax = propertyTax;
        this.size = size;
        this.rating = rating;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public double getPropertyTax() {
        return propertyTax;
    }

    public void setPropertyTax(double propertyTax) {
        this.propertyTax = propertyTax;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}