# Hotel Management System

This project is a simple hotel management system implemented in Java. It includes various classes representing different entities in the system, such as `CEO`, `FranchiseAgreement`, `Hotel`, `CleaningStaff`, `Receptionist`, `HR`, `Payroll`, `Room`, `Booking`, `PointOfSale`, `LoyaltyProgram`, `PaymentMethod`, `Customer`, and `FranchiseOwner`.

## Project Structure

The project is organized into multiple Java files, each representing a different class:

- `Controllers`
  - `BookingController.java`
  - `CustomerController.java`
  - `HotelController.java`
  - `PaymentController.java`
- `Models`
  - `Booking.java`
  - `CEO.java`
  - `CleaningStaff.java`
  - `Customer.java`
  - `FranchiseAgreement.java`
  - `FranchiseOwner.java`
  - `HR.java`
  - `Hotel.java`
  - `LoyaltyProgram.java`
  - `PaymentMethod.java`
  - `Payroll.java`
  - `Person.java`
  - `PointOfSale.java`
  - `Receptionist.java`
  - `Room.java`
- `Storage`
  - `BookingStorageHelper.java`
  - `CustomerStorageHelper.java`
  - `HotelStorageHelper.java`
  - `PaymentStorageHelper.java`
- `UseCases`
  - `UseCases.java`

## How to Run

To run the use case tests, follow these steps:

1. Ensure you have Java installed on your machine.
2. Navigate to the root directory of the project:

    ```sh
    cd "c:\Users\gabeb\Desktop\362"
    ```

3. Compile all the Java files. You can use the following command in the terminal:

    ```sh
    javac -d bin Controllers/*.java Models/*.java Storage/*.java UseCases/UseCases.java
    ```

4. Run the `UseCases` class to see the output of the use case tests:

    ```sh
    java -cp bin UseCases
    ```

## Example Output

When you run the `UseCases` class, you should see output similar to the following:
Select a use case to run:

Create Booking
Get Booking
Update Booking
Delete Booking
Create Customer
Get Customer
Update Customer
Delete Customer
Create Payment Method
Get Payment Method
Exit


## Classes and Attributes

### CEO
- `id`: int
- `name`: String
- `contactInfo`: String
- `numberOfFranchisesManaged`: int
- `grossProfit`: double
- `feesCosts`: double

### FranchiseAgreement
- `id`: int
- `startDate`: String
- `endDate`: String
- `fees`: double
- `conditions`: String

### Hotel
- `id`: int
- `name`: String
- `location`: String
- `roomCount`: int
- `propertyTax`: double
- `size`: double
- `rating`: double

### CleaningStaff
- `id`: int
- `name`: String
- `contactInfo`: String
- `wage`: double
- `shiftHours`: String
- `experience`: int

### Receptionist
- `id`: int
- `name`: String
- `contactInfo`: String
- `wage`: double
- `shiftHours`: String
- `experience`: int

### HR
- `id`: int
- `name`: String
- `wageBudget`: double
- `numberOfEmployeesManaged`: int
- `policiesImplemented`: String

### Payroll
- `id`: int
- `employeeId`: int
- `salary`: double
- `bonuses`: double
- `dateIssued`: String
- `paymentStatus`: String

### Room
- `id`: int
- `bedCount`: int
- `roomType`: String
- `pricePerNight`: double
- `status`: String

### Booking
- `id`: int
- `checkInDate`: String
- `checkOutDate`: String
- `totalPrice`: double
- `paymentStatus`: String
- `room`: Room

### PointOfSale
- `id`: int
- `operator`: String
- `totalSales`: double
- `validationSystem`: String

### LoyaltyProgram
- `id`: int
- `tier`: String
- `pointsAccumulated`: int
- `rewardsAvailable`: String

### PaymentMethod
- `id`: int
- `type`: String
- `cardNumber`: String
- `expiryDate`: String
- `paymentStatus`: String

### Customer
- `id`: int
- `name`: String
- `contactInfo`: String
- `loyaltyProgramLevel`: String
- `paymentMethod`: String
- `numberOfStays`: int

### FranchiseOwner
- `id`: int
- `name`: String
- `grossProfit`: double
- `hotelSize`: double
- `numberOfHotelsOwned`: int
- `contactInfo`: String