package Models.SpaReservation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SpaReservation {
    private int id;
    private boolean facial;
    private boolean massageBasic;
    private boolean massagePremium;
    private boolean manicure;
    private boolean pedicure;
    private double startTime;
    private double endTime;

    public SpaReservation(int id, boolean facial, boolean massageBasic, boolean massagePremium, boolean manicure, boolean pedicure, double startTime, double endTime) {
        this.id = id;
        this.facial = facial;
        this.massageBasic = massageBasic;
        this.massagePremium = massagePremium;
        this.manicure = manicure;
        this.pedicure = pedicure;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public SpaReservation(Map<String, Object> map) {
        this.id = ((Number) map.get("id")).intValue();
        this.facial = (boolean) map.get("facial");
        this.massageBasic = (boolean) map.get("massageBasic");
        this.massagePremium = (boolean) map.get("massagePremium");
        this.manicure = (boolean) map.get("manicure");
        this.pedicure = (boolean) map.get("pedicure");
        this.startTime = ((Number) map.get("startTime")).doubleValue();
        this.endTime = ((Number) map.get("endTime")).doubleValue();
    }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("facial", facial);
        map.put("massageBasic", massageBasic);
        map.put("massagePremium", massagePremium);
        map.put("manicure", manicure);
        map.put("pedicure", pedicure);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        return map;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFacial() {
        return facial;
    }

    public void setFacial(boolean facial) {
        this.facial = facial;
    }

    public boolean isMassageBasic() {
        return massageBasic;
    }

    public void setMassageBasic(boolean massageBasic) {
        this.massageBasic = massageBasic;
    }

    public boolean isMassagePremium() {
        return massagePremium;
    }

    public void setMassagePremium(boolean massagePremium) {
        this.massagePremium = massagePremium;
    }

    public boolean isManicure() {
        return manicure;
    }

    public void setManicure(boolean manicure) {
        this.manicure = manicure;
    }

    public boolean isPedicure() {
        return pedicure;
    }

    public void setPedicure(boolean pedicure) {
        this.pedicure = pedicure;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }
}
