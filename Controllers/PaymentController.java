package Controllers;

import java.io.IOException;

import Models.PaymentMethod;
import Storage.StorageHelper;

import java.util.Map;

public class PaymentController {
    private StorageHelper storageHelper;

    public PaymentController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, "payment_methods");
    }

    public void createPaymentMethod(PaymentMethod paymentMethod) throws IOException {
        storageHelper.getStore("payment_methods").save(String.valueOf(paymentMethod.getId()), paymentMethod.toMap());
    }

    public PaymentMethod getPaymentMethod(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore("payment_methods").load(String.valueOf(id));
        return data != null ? new PaymentMethod().fromMap(data) : null;
    }

    public void updatePaymentMethod(PaymentMethod paymentMethod) throws IOException {
        storageHelper.getStore("payment_methods").save(String.valueOf(paymentMethod.getId()), paymentMethod.toMap());
    }

    public void deletePaymentMethod(int id) throws IOException {
        storageHelper.getStore("payment_methods").delete(String.valueOf(id));
    }
}