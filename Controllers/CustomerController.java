package Controllers;

import java.io.IOException;

import Models.Customer;
import storage.StorageHelper;

import java.util.Map;

public class CustomerController {
    private StorageHelper storageHelper;

    public CustomerController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, "customers");
    }

    public void createCustomer(Customer customer) throws IOException {
        storageHelper.getStore("customers").save(String.valueOf(customer.getId()), customer.toMap());
    }

    public Customer getCustomer(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore("customers").load(String.valueOf(id));
        return data != null ? (Customer) new Customer().fromMap(data) : null;
    }

    public void updateCustomer(Customer customer) throws IOException {
        storageHelper.getStore("customers").save(String.valueOf(customer.getId()), customer.toMap());
    }

    public void deleteCustomer(int id) throws IOException {
        storageHelper.getStore("customers").delete(String.valueOf(id));
    }
}