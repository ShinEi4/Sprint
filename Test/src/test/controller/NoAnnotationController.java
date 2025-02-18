package test.controller;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.model.ModelView;
import ituprom16.framework.annotation.GET;

@AnnotationController
public class NoAnnotationController {
    
    // Méthode sans annotation @GET ni @POST
    public ModelView testNoAnnotation() {
        ModelView mv = new ModelView("/result.jsp");
        mv.addObject("message", "Cette méthode n'a pas d'annotation");
        return mv;
    }
    
    // Méthode avec annotation pour comparaison
    @GET("/with-annotation")
    public ModelView testWithAnnotation() {
        ModelView mv = new ModelView("/result.jsp");
        mv.addObject("message", "Cette méthode a une annotation @GET");
        return mv;
    }
} 