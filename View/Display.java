package View;

public class Display {

    public void DisplayActors() {
        System.out.println("1. Manager");
        System.out.println("2. Maintenance Staff");
        System.out.println("3. Kitchen Staff");
        System.out.println("4. Customer");
        System.out.println("5. Cleaning Staff");
        System.out.println("6. Applicant");
        System.out.println("7. Grounds Keeper");
        System.out.println("8. Travel Desk");
        System.out.println("0. Exit");
    }

    public void DisplayApplicantUseCases() {
        System.out.println("1. Apply to Job Posting");
        System.out.println("2. Accept or Decline Job Offer");
        System.out.println("0. Exit");
    }

    public void DisplayGroundsKeeperUseCases() {
        System.out.println("1. Create Grounds Inspection");
        System.out.println("2. Modify Grounds Inspection");
        System.out.println("3. Generate Inspection Report");
        System.out.println("4. Log Unexpected Issues");
        System.out.println("5. Validate and Replace Grounds Log");
        System.out.println("6. Cancel Grounds Inspection");
        System.out.println("0. Exit to change your Actor choice");
    }

    public void DisplayCleaningStaffUseCases() {
        System.out.println("1. Clean Room");
        System.out.println("0. Exit to change your Actor choice");
    }

    public void DisplayKitchenStaffUseCases() {
        System.out.println("==========Breakfast actions==========");
        System.out.println("1. Make Breakfast");
        System.out.println("2. Add Ingredient");
        System.out.println("3. Subtract Ingredient (do not use for normal breakfast cooking)");
        System.out.println("4. Set Ingredient Amount (do not use for normal breakfast cooking)");
        System.out.println("5. View All Breakfast Reports");
        System.out.println("6. Delete a Breakfast Report");
        System.out.println("7. View current Ingredient stock");
        System.out.println("8. Check Ingredient stock for gusts");
        System.out.println("0. Exit to change your Actor choice");
    }

    public void DisplayMaintenanceEmployeeUseCases() {
        System.out.println("==========Pool Actions==========");
        System.out.println("1. Complete New Pool Inspection");
        System.out.println("2. View All Pool Inspections");
        System.out.println("3. Complete New Chemical Reading");
        System.out.println("4. View All Chemical Readings and current Chemical Inventory");
        System.out.println("5. Add to Chemical Inventory");
        System.out.println("6. Subtract from Chemical Inventory");
        System.out.println("7. Set Chemical Inventory");
        System.out.println("==========Breakfast actions==========");
        System.out.println("8. Add Ingredient");
        System.out.println("9. Subtract Ingredient (do not use for normal breakfast cooking)");
        System.out.println("10. Set Ingredient Amount (do not use for normal breakfast cooking)");
        System.out.println("11. View current Ingredient stock");
        System.out.println("0. Exit to change your Actor choice");
    }

    public void DisplayCustomerUseCases() {
        System.out.println("1. Book Hotel Room");
        System.out.println("2. Check out of Hotel Room");
        System.out.println("3. Book Room with Rewards");
        System.out.println("4. Vending");
        System.out.println("5. Access Facility");
        System.out.println("6. Room Service");
        System.out.println("==========Spa actions==========");
        System.out.println("7. Reserve spa");
        System.out.println("8. View Reservations for a Specific Day");
        System.out.println("==========Manage bikes actions==========");
        System.out.println("9. Check in a bike");
        System.out.println("10. Check out a bike");
        System.out.println("11. See current bike fleet");
        System.out.println("==========Event Booking Actions==========");
        System.out.println("12. Book Event Room");
        System.out.println("13. Cancel Event Room Booking");
        System.out.println("=========Excursions========");
        System.out.println("14. Book Excursion");
        System.out.println("15. Go on Excursion!");
        System.out.println("16. Cancel Excursion");
        System.out.println("0. Exit to change your Actor choice");
    }

    public void DisplayTravelDeskAgentUseCases() {
        System.out.println("1. View Excursions");
        System.out.println("2. Create Excursion");
        System.out.println("3. Delete Excursion");
        System.out.println("4. View Current Bookings");
        System.out.println("0. Exit");
    }

    public void DisplayManagerUseCases() {
        System.out.println("==========Pool Actions==========");
        System.out.println("1. Complete New Pool Inspection");
        System.out.println("2. View All Pool Inspections");
        System.out.println("3. Complete New Chemical Reading");
        System.out.println("4. View All Chemical Readings and current Chemical Inventory");
        System.out.println("5. Add to Chemical Inventory");
        System.out.println("6. Subtract from Chemical Inventory");
        System.out.println("7. Set Chemical Inventory");
        System.out.println("==========Breakfast actions==========");
        System.out.println("8. Make Breakfast");
        System.out.println("9. Add Ingredient");
        System.out.println("10. Subtract Ingredient (do not use for normal breakfast cooking)");
        System.out.println("11. Set Ingredient Amount (do not use for normal breakfast cooking)");
        System.out.println("12. View All Breakfast Reports");
        System.out.println("13. Delete a Breakfast Report");
        System.out.println("14. View current Ingredient stock");
        System.out.println("15. Check Ingredient stock for gusts");
        System.out.println("0. Exit to change your Actor choice");
        System.out.println("==========Franchise actions==========");
        System.out.println("16. Demonstrate Profit Cycle");
        System.out.println("17. Sign Franchise Agreement");
        System.out.println("==========Vending actions==========");
        System.out.println("18. Add Item to Vending Machine");
        System.out.println("19. Restock the Vending Machine");
        System.out.println("=========Other actions========");
        System.out.println("20. Access Facility");
        System.out.println("==========Clean facility actions==========");
        System.out.println("23. Do facility cleaning");
        System.out.println("24. view all");
        System.out.println("25. Set cleaning frequency");
        System.out.println("==========Work request actions==========");
        System.out.println("26. Create a Work Request");
        System.out.println("27. View All Work Requests");
        System.out.println("28. Complete a Work Request");
        System.out.println("==========Manage current cleaning inventory actions==========");
        System.out.println("29. View current inventory of cleaning supplies");
        System.out.println("30. Set current inventory of cleaning supplies");
        System.out.println("==========Manage bikes actions==========");
        System.out.println("31. Add a bike");
        System.out.println("32. Maintain a bike");
        System.out.println("33. Check in a bike");
        System.out.println("34. Check out a bike");
        System.out.println("35. See current bike fleet");
        System.out.println("==========Spa actions==========");
        System.out.println("36. Reset for Spa for next week");
        System.out.println("37. Reserve spa");
        System.out.println("38. View Reservations for a Specific Day");
        System.out.println("==========Marketing actions==========");
        System.out.println("39. Send marketing email");
        System.out.println("==========Hiring actions==========");
        System.out.println("40. Request New Hire");
        System.out.println("41. Shortlist Applicants");
        System.out.println("42. Interview Shortlisted Applicants");
        System.out.println("43. Select Candidate");
        System.out.println("44. Cancel Hiring Process");
        System.out.println("45. Schedule interview");
        System.out.println("==========Payroll actions==========");
        System.out.println("46. Verify Employee Payroll Info");
        System.out.println("47. Pay Employee");
        System.out.println("0. Exit to change your Actor choice");
    }

}
