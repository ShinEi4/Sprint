package test.controller;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.annotation.GET;
import ituprom16.framework.model.ModelView;

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
    public ModelView getEmployeeDetails() {
        ModelView mv = new ModelView("/employees.jsp");
        mv.addObject("title", "Détails des employés");
        mv.addObject("count", 3);
        mv.addObject("names", new String[]{"Jean", "Marie", "Pierre"});
        return mv;
    }
} 