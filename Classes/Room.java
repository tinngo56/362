public class Room {
    private int id;
    private int bedCount;
    private String roomType;
    private double pricePerNight;
    private String status;

    public Room(int id, int bedCount, String roomType, double pricePerNight, String status) {
        this.id = id;
        this.bedCount = bedCount;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.status = status;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBedCount() {
        return bedCount;
    }

    public void setBedCount(int bedCount) {
        this.bedCount = bedCount;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}