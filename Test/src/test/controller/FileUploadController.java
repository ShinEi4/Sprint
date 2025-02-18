package test.controller;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.annotation.POST;
import ituprom16.framework.annotation.GET;
import ituprom16.framework.annotation.Param;
import ituprom16.framework.model.ModelView;
import java.util.Base64;

@AnnotationController
public class FileUploadController {
    
    @POST("/upload")
    public ModelView uploadFile(
        @Param(name="file") byte[] fileContent,
        @Param(name="file") String fileName
    ) {
        ModelView mv = new ModelView("/upload-result.jsp");
        mv.addObject("fileName", fileName);
        mv.addObject("fileSize", fileContent.length);
        // Pour l'affichage des images
        if (fileName.toLowerCase().endsWith(".jpg") || 
            fileName.toLowerCase().endsWith(".png")) {
            String base64Image = Base64.getEncoder().encodeToString(fileContent);
            mv.addObject("imageData", base64Image);
        }
        return mv;
    }

    @GET("/upload-form")
    public ModelView showUploadForm() {
        return new ModelView("/upload-form.jsp");
    }
} 