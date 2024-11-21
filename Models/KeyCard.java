package Models;

public class KeyCard extends Mappable<KeyCard> {

    private AccessLevels accessLevel;
    private String cardNumber;
    private int bookingNum;
    private int customerId;

    public KeyCard(AccessLevels accessLevel, String cardNumber, int bookingNum, int customerId) {
        this.accessLevel = accessLevel;
        this.cardNumber = cardNumber;
        this.bookingNum = bookingNum;
        this.customerId = customerId;
    }

    public KeyCard() { super();}

    public void setCardNumber(String number) {
        this.cardNumber = number;
    }

    public AccessLevels getAccessLevel() {
        return accessLevel;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Gets the booking number.
     * @return Booking number. -1 for no booking.
     */
    public int getBookingNum() {
        return bookingNum;
    }

    public int getCustomerId() {
        return customerId;
    }

}
