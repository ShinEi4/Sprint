package test.controller;

import ituprom16.framework.annotation.AnnotationController;

@AnnotationController
public class TestController {
    private String name = "TestController";
    
    public String getName() {
        return name;
    }
} 