package com.quizserver.controller;

import com.quizserver.config.QuizBetaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin("*")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private QuizBetaConfig quizBetaConfig;

    @GetMapping("/status")
    public ResponseEntity<?> getHealthStatus() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            // Check database connection
            Connection connection = dataSource.getConnection();
            boolean isDatabaseConnected = connection != null && !connection.isClosed();
            connection.close();
            
            health.put("status", "UP");
            health.put("database", isDatabaseConnected ? "Connected" : "Disconnected");
            health.put("databaseName", quizBetaConfig.getDatabaseName());
            health.put("application", quizBetaConfig.getAppName());
            health.put("version", quizBetaConfig.getVersion());
            health.put("description", quizBetaConfig.getDescription());
            health.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(health);
            
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("database", "Error: " + e.getMessage());
            health.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(503).body(health);
        }
    }

    @GetMapping("/db-info")
    public ResponseEntity<?> getDatabaseInfo() {
        Map<String, Object> dbInfo = new HashMap<>();
        
        try {
            Connection connection = dataSource.getConnection();
            
            dbInfo.put("databaseName", quizBetaConfig.getDatabaseName());
            dbInfo.put("databaseURL", connection.getMetaData().getURL());
            dbInfo.put("driverName", connection.getMetaData().getDriverName());
            dbInfo.put("driverVersion", connection.getMetaData().getDriverVersion());
            dbInfo.put("connected", true);
            
            connection.close();
            
            return ResponseEntity.ok(dbInfo);
            
        } catch (Exception e) {
            dbInfo.put("error", e.getMessage());
            dbInfo.put("connected", false);
            
            return ResponseEntity.status(503).body(dbInfo);
        }
    }
}
