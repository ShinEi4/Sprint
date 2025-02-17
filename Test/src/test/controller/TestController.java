package test.controller;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.annotation.GET;
import ituprom16.framework.annotation.Param;
import ituprom16.framework.model.ModelView;

@AnnotationController
public class TestController {
    private String name = "TestController";
    
    public String getName() {
        return name;
    }
    
    @GET("/test")
    public String test() {
        return "Page de test";
    }
    
    @GET("/hello")
    public String hello() {
        return "Hello World";
    }
    
    @GET("/form")
    public ModelView showForm() {
        return new ModelView("/form.jsp");
    }
    
    @GET("/save-name")
    public ModelView saveName(@Param(name="userName") String name) {
        ModelView mv = new ModelView("/result.jsp");
        mv.addObject("name", name);
        mv.addObject("message", "Bonjour " + name + " !");
        return mv;
    }
} 