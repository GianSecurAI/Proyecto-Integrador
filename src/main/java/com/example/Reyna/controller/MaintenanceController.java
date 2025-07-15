package com.example.Reyna.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MaintenanceController {

    @GetMapping("/maintenance")
    @ResponseBody
    public String maintenancePage() {
        return "{\"status\":\"maintenance\",\"message\":\"Sistema en mantenimiento programado. Por favor, intente más tarde.\"}";
    }
}