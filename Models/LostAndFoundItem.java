package Models;

import java.time.LocalDateTime;

public class LostAndFoundItem extends Mappable<LostAndFoundItem> {
    private String itemId;
    private String roomNumber;
    private String description;
    private LocalDateTime foundTime;
    private String foundBy;
    private String status;

    public LostAndFoundItem() {
        this.foundTime = LocalDateTime.now();
        this.status = "FOUND";
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getFoundTime() {
        return foundTime;
    }
    
    public String getFoundBy() {
        return foundBy;
    }

    public void setFoundBy(String foundBy) {
        this.foundBy = foundBy;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestContact() {
        return guestContact;
    }

    public void setGuestContact(String guestContact) {
        this.guestContact = guestContact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
