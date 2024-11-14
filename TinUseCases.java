import java.io.IOException;

import Controllers.CleaningStaffController;
import Models.CleaningStaff;

public class TinUseCases {
    public static void main(String[] args) throws IOException {
        CleaningStaffController cleaningStaffController = new CleaningStaffController("hotel_data");
        CleaningStaff cleaningStaff = cleaningStaffController.getAvailableCleaningStaff();
        cleaningStaffController.cleanRoom("102", cleaningStaff);
    }    
}
