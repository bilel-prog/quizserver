package com.quizserver.service.data;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Order(2) // Execute after TestDataInitializer
public class DataInitializerCleanup {

    @Autowired
    private TestDataInitializer testDataInitializer;

    @PostConstruct
    public void cleanupAfterDataLoad() {
        // Wait a bit to ensure data initialization is complete
        try {
            Thread.sleep(2000); // Wait 2 seconds
            
            // Get the path to the TestDataInitializer file
            String userDir = System.getProperty("user.dir");
            Path initializerPath = Paths.get(userDir, "src", "main", "java", "com", "quizserver", "service", "data", "TestDataInitializer.java");
            Path cleanupPath = Paths.get(userDir, "src", "main", "java", "com", "quizserver", "service", "data", "DataInitializerCleanup.java");
            
            if (Files.exists(initializerPath)) {
                System.out.println("=== Cleaning up data initialization files ===");
                
                // Delete the TestDataInitializer file
                Files.delete(initializerPath);
                System.out.println("Deleted: TestDataInitializer.java");
                
                // Delete this cleanup file as well
                Files.delete(cleanupPath);
                System.out.println("Deleted: DataInitializerCleanup.java");
                
                System.out.println("=== Data initialization cleanup complete ===");
                System.out.println("Test data has been permanently added to the database.");
                System.out.println("Initialization files have been removed to prevent duplicate data.");
            }
            
        } catch (InterruptedException | IOException e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }
}
