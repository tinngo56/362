package util;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriterUtility {

    private String filePath;

    public FileWriterUtility(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Writes to the file
     * @param logEntry text to be written
     */
    public void writeToFile(String logEntry) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(logEntry + "\n");
            System.out.println("Log entry written to file: " + logEntry);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Logs pool maintenance
     * @param maintenanceSuccess true if maintenance succes else false
     * @param message message or null if success
     */
    public void writeToPoolSave(boolean maintenanceSuccess, String message) {
        if (maintenanceSuccess) {
            writeToFile("Maintenance completed successfully for pool facility.");
            writeToFile("Note: " + message);
        } else {
            writeToFile("Maintenance failed to meet standards for pool facility.");
            writeToFile("Issue: " + message);
        }
    }

}
