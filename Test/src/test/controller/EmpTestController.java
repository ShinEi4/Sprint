package test.controller;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.annotation.GET;
import ituprom16.framework.annotation.ModelAttribute;
import ituprom16.framework.model.ModelView;
import test.model.Emp;

@AnnotationController
public class EmpTestController {
    
    @GET("/emp-form")
    public ModelView showEmpForm() {
        return new ModelView("/emp-form.jsp");
    }
    
    @GET("/save-emp")
    public ModelView saveEmp(@ModelAttribute Emp emp) {
        ModelView mv = new ModelView("/emp-details.jsp");
        mv.addObject("employee", emp);
        mv.addObject("message", "Employé enregistré avec succès !");
        return mv;
    }
} 