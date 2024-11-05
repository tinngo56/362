package Controllers;

import java.io.IOException;

import Models.PaymentMethod;
import Storage.PaymentStorageHelper;
import java.util.Map;

public class PaymentController {
    private PaymentStorageHelper paymentStorageHelper;

    public PaymentController(String baseDirectory) throws IOException {
        this.paymentStorageHelper = new PaymentStorageHelper(baseDirectory);
    }

    public void createPaymentMethod(PaymentMethod paymentMethod) throws IOException {
        paymentStorageHelper.getStore("payment_methods").save(String.valueOf(paymentMethod.getId()), paymentMethod.toMap());
    }

    public PaymentMethod getPaymentMethod(int id) throws IOException {
        Map<String, Object> data = paymentStorageHelper.getStore("payment_methods").load(String.valueOf(id));
        return data != null ? PaymentMethod.fromMap(data) : null;
    }

    public void updatePaymentMethod(PaymentMethod paymentMethod) throws IOException {
        paymentStorageHelper.getStore("payment_methods").save(String.valueOf(paymentMethod.getId()), paymentMethod.toMap());
    }

    public void deletePaymentMethod(int id) throws IOException {
        paymentStorageHelper.getStore("payment_methods").delete(String.valueOf(id));
    }
}