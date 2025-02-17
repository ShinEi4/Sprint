package test.controller;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.annotation.GET;
import ituprom16.framework.model.ModelView;

@AnnotationController
public class ErrorTestController {
    
    // Test avec ModelView
    @GET("/invalid-return")
    public int invalidReturnType() {
        return 42;
    }
    
    // Test URL en double (même URL que TestController/test)
    @GET("/test")
    public String duplicateUrl() {
        return "Test URL en double";
    }
    
    // Test avec String
    @GET("/test")
    public String testError() {
        return "Page de test d'erreur";
    }
} 