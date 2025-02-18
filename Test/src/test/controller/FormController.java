package test.controller;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.annotation.GET;
import ituprom16.framework.annotation.POST;
import ituprom16.framework.annotation.Param;
import ituprom16.framework.model.ModelView;

@AnnotationController
public class FormController {
    
    @GET("/form")
    public ModelView showForm() {
        return new ModelView("/form.jsp");
    }
    
    @POST("/submit")
    public ModelView submitForm(@Param(name="name") String name) {
        ModelView mv = new ModelView("/result.jsp");
        mv.addObject("message", "Formulaire soumis avec succès");
        mv.addObject("name", name);
        return mv;
    }
} 