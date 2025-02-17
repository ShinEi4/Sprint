package test.controller;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.annotation.GET;

@AnnotationController
public class EmpController {
    
    @GET("/all")
    public String getAllEmployees() {
        return "Liste de tous les employés : Jean, Marie, Pierre";
    }
    
    @GET("/count")
    public String getEmployeeCount() {
        return "Nombre total d'employés : 3";
    }
    
    @GET("/details")
    public String getEmployeeDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Détails des employés :<br>");
        details.append("1. Jean - Développeur<br>");
        details.append("2. Marie - Designer<br>");
        details.append("3. Pierre - Manager");
        return details.toString();
    }
} 