package test.controller;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.annotation.GET;
import ituprom16.framework.annotation.POST;
import ituprom16.framework.annotation.Param;
import ituprom16.framework.annotation.Required;
import ituprom16.framework.annotation.Min;
import ituprom16.framework.annotation.Max;
import ituprom16.framework.annotation.Email;
import ituprom16.framework.annotation.FormUrl;
import ituprom16.framework.model.ModelView;

@AnnotationController
public class ValidationTestController {
    
    @GET("/validation-form")
    public ModelView showForm() {
        return new ModelView("/validation-form.jsp");
    }
    
    @POST("/validate")
    @FormUrl("/ValidationTestController/validation-form")
    public ModelView validateForm(
        @Required
        @Param(name="name") String name,
        
        @Required
        @Min(value=18)
        @Max(value=100)
        @Param(name="age") int age,
        
        @Required
        @Email
        @Param(name="email") String email
    ) {
        ModelView mv = new ModelView("/validation-result.jsp");
        mv.addObject("name", name);
        mv.addObject("age", age);
        mv.addObject("email", email);
        return mv;
    }
} 