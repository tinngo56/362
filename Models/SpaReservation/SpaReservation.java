package Models.SpaReservation;

import java.util.HashMap;
import java.util.Map;

public class SpaReservation {
    private int id;
    private String name; // Customer's name
    private boolean facial;
    private boolean massageBasic;
    private boolean massagePremium;
    private boolean manicure;
    private boolean pedicure;
    private double startTime;
    private double endTime;
    private DayOfWeek day; // Day of the reservation

    // Primary Constructor
    public SpaReservation(int id, String name, boolean facial, boolean massageBasic, boolean massagePremium,
                          boolean manicure, boolean pedicure, double startTime, double endTime, int dayIndex) {
        this.id = id;
        this.name = name;
        this.facial = facial;
        this.massageBasic = massageBasic;
        this.massagePremium = massagePremium;
        this.manicure = manicure;
        this.pedicure = pedicure;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = DayOfWeek.fromInt(dayIndex);
    }

    // Constructor for deserialization
    public SpaReservation(Map<String, Object> map) {
        this.id = ((Number) map.get("id")).intValue();
        this.name = (String) map.get("name");
        this.facial = (Boolean) map.get("facial");
        this.massageBasic = (Boolean) map.get("massageBasic");
        this.massagePremium = (Boolean) map.get("massagePremium");
        this.manicure = (Boolean) map.get("manicure");
        this.pedicure = (Boolean) map.get("pedicure");
        this.startTime = ((Number) map.get("startTime")).doubleValue();
        this.endTime = ((Number) map.get("endTime")).doubleValue();
        int dayIndex = ((Number) map.get("day")).intValue();
        this.day = DayOfWeek.fromInt(dayIndex);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isFacial() {
        return facial;
    }

    public boolean isMassageBasic() {
        return massageBasic;
    }

    public boolean isMassagePremium() {
        return massagePremium;
    }

    public boolean isManicure() {
        return manicure;
    }

    public boolean isPedicure() {
        return pedicure;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFacial(boolean facial) {
        this.facial = facial;
    }

    public void setMassageBasic(boolean massageBasic) {
        this.massageBasic = massageBasic;
    }

    public void setMassagePremium(boolean massagePremium) {
        this.massagePremium = massagePremium;
    }

    public void setManicure(boolean manicure) {
        this.manicure = manicure;
    }

    public void setPedicure(boolean pedicure) {
        this.pedicure = pedicure;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    // Serialize to Map
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("facial", facial);
        map.put("massageBasic", massageBasic);
        map.put("massagePremium", massagePremium);
        map.put("manicure", manicure);
        map.put("pedicure", pedicure);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("day", day.ordinal());
        return map;
    }
}