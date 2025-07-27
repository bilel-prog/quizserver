package com.quizserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "quiz.beta")
public class QuizBetaConfig {
    
    private String appName = "Quiz Beta Application";
    private String version = "1.0.0";
    private String description = "A comprehensive quiz application with user management and secure authentication";
    
    // Database specific configurations
    private String databaseName = "quiz_beta_db";
    private boolean enableDataInitialization = true;
    
    // Security configurations
    private boolean enableSecurityLogs = true;
    private int maxLoginAttempts = 5;
    
    // Getters and Setters
    public String getAppName() {
        return appName;
    }
    
    public void setAppName(String appName) {
        this.appName = appName;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDatabaseName() {
        return databaseName;
    }
    
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    
    public boolean isEnableDataInitialization() {
        return enableDataInitialization;
    }
    
    public void setEnableDataInitialization(boolean enableDataInitialization) {
        this.enableDataInitialization = enableDataInitialization;
    }
    
    public boolean isEnableSecurityLogs() {
        return enableSecurityLogs;
    }
    
    public void setEnableSecurityLogs(boolean enableSecurityLogs) {
        this.enableSecurityLogs = enableSecurityLogs;
    }
    
    public int getMaxLoginAttempts() {
        return maxLoginAttempts;
    }
    
    public void setMaxLoginAttempts(int maxLoginAttempts) {
        this.maxLoginAttempts = maxLoginAttempts;
    }
}
