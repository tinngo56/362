package maintainPool;

import java.util.HashMap;
import java.util.Map;

public class ChemicalReading {

    private Map<String, Float> chemicals;
    private Map<String, Float[]> acceptableRanges;

    public ChemicalReading() {
        chemicals = new HashMap<>();
        acceptableRanges = new HashMap<>();

        chemicals.put("Chlorine", 0.0f);
        chemicals.put("pH", 0.0f);
        chemicals.put("Alkalinity", 0.0f);

        acceptableRanges.put("Chlorine", new Float[]{1.0f, 3.0f});
        acceptableRanges.put("pH", new Float[]{7.2f, 7.8f});
        acceptableRanges.put("Alkalinity", new Float[]{80.0f, 120.0f});
    }

    public void setChemicalReading(String chemical, float reading) {
        if (chemicals.containsKey(chemical)) {
            chemicals.put(chemical, reading);
        }
    }

    /**
     * Checks if all chemical readings are within their acceptable ranges
     * @return a string listing chemicals out of range or null if all are within
     */
    public String isWithinAcceptableRange() {
        String outOfRangeChemicals = "";

        for (Map.Entry<String, Float> entry : chemicals.entrySet()) {
            String chemical = entry.getKey();
            float reading = entry.getValue();
            Float[] range = acceptableRanges.get(chemical);

            if (reading < range[0] || reading > range[1]) {
                outOfRangeChemicals += chemical + " out of range; ";
            }
        }

        if (outOfRangeChemicals.isEmpty()) {
            return null;
        } else {
            return outOfRangeChemicals;
        }
    }
}
