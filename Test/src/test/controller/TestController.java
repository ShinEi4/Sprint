package test.controller;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.annotation.GET;

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
} 