package Controllers;

import Models.FranchiseAgreement;
import Storage.StorageHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FranchiseAgreementController {
    private StorageHelper storageHelper;
    private final String STORE_NAME = "franchise_agreements";

    public FranchiseAgreementController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, STORE_NAME);
    }

    public void createFranchiseAgreement(FranchiseAgreement agreement) throws IOException {
        Map<String, Object> agreementMap = convertAgreementToMap(agreement);
        storageHelper.getStore(STORE_NAME).save(String.valueOf(agreement.getId()), agreementMap);
    }

    public FranchiseAgreement getFranchiseAgreement(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore(STORE_NAME).load(String.valueOf(id));
        return data != null ? convertMapToAgreement(data) : null;
    }

    public int getNumOfAgreements() throws IOException {
        return storageHelper.getStore(STORE_NAME).loadAll().size();
    }

    private Map<String, Object> convertAgreementToMap(FranchiseAgreement agreement) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", agreement.getId());
        map.put("startDate", agreement.getStartDate());
        map.put("endDate", agreement.getEndDate());
        map.put("fees", agreement.getFees());
        map.put("conditions", agreement.getConditions());
        return map;
    }

    private FranchiseAgreement convertMapToAgreement(Map<String, Object> map) {
        int id = (Integer) map.get("id");
        String startDate = (String) map.get("startDate");
        String endDate = (String) map.get("endDate");
        double fees = (Double) map.get("fees");
        String conditions = (String) map.get("conditions");
        return new FranchiseAgreement(id, startDate, endDate, fees, conditions);
    }
}