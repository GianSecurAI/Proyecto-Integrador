package com.example.Reyna.security;

import org.springframework.stereotype.Component;
import java.time.LocalTime;

@Component
public class AccessTimeManager {
    
    private boolean accessRestricted = false;
    
    public boolean isAccessAllowed() {
        return !accessRestricted;
    }
    
    public void restrictAccess() {
        this.accessRestricted = true;
    }
    
    public void allowAccess() {
        this.accessRestricted = false;
    }
    
    public boolean isMaintenanceTime() {
        LocalTime now = LocalTime.now();
        LocalTime startRestriction = LocalTime.of(1, 0); // 1:00 AM
        LocalTime endRestriction = LocalTime.of(4, 0);   // 4:00 AM
        
        return now.isAfter(startRestriction) && now.isBefore(endRestriction);
    }
}
