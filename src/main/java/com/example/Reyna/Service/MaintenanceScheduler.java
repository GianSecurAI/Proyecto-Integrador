package com.example.Reyna.Service;

import com.example.Reyna.security.AccessTimeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceScheduler {

    private final AccessTimeManager accessTimeManager;
    
    @Autowired
    public MaintenanceScheduler(AccessTimeManager accessTimeManager) {
        this.accessTimeManager = accessTimeManager;
    }
    
    // Se ejecuta a la 1:00 AM todos los días
    @Scheduled(cron = "0 0 1 * * ?")
    public void startMaintenance() {
        accessTimeManager.restrictAccess();
        System.out.println("Sistema en mantenimiento - Acceso restringido");
    }
    
    // Se ejecuta a las 4:00 AM todos los días
    @Scheduled(cron = "0 0 4 * * ?")
    public void endMaintenance() {
        accessTimeManager.allowAccess();
        System.out.println("Mantenimiento finalizado - Acceso permitido");
    }
}
