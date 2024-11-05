package Controllers;

import java.io.IOException;

import Models.Customer;
import Storage.CustomerStorageHelper;
import java.util.Map;

public class CustomerController {
    private CustomerStorageHelper customerStorageHelper;

    public CustomerController(String baseDirectory) throws IOException {
        this.customerStorageHelper = new CustomerStorageHelper(baseDirectory);
    }

    public void createCustomer(Customer customer) throws IOException {
        customerStorageHelper.getStore("customers").save(String.valueOf(customer.getId()), customer.toMap());
    }

    public Customer getCustomer(int id) throws IOException {
        Map<String, Object> data = customerStorageHelper.getStore("customers").load(String.valueOf(id));
        return data != null ? Customer.fromMap(data) : null;
    }

    public void updateCustomer(Customer customer) throws IOException {
        customerStorageHelper.getStore("customers").save(String.valueOf(customer.getId()), customer.toMap());
    }

    public void deleteCustomer(int id) throws IOException {
        customerStorageHelper.getStore("customers").delete(String.valueOf(id));
    }
}