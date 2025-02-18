package test.controller;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.annotation.GET;
import ituprom16.framework.annotation.RestAPI;
import ituprom16.framework.model.ModelView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AnnotationController
public class RestTestController {
    
    @GET("/api/data")
    @RestAPI
    public List<String> getData() {
        List<String> data = new ArrayList<>();
        data.add("Item 1");
        data.add("Item 2");
        data.add("Item 3");
        return data;
    }
    
    @GET("/api/employee")
    @RestAPI
    public ModelView getEmployee() {
        ModelView mv = new ModelView();
        Map<String, Object> employee = new HashMap<>();
        employee.put("name", "John Doe");
        employee.put("age", 30);
        employee.put("position", "Developer");
        
        mv.addObject("data", employee);
        return mv;
    }
} 