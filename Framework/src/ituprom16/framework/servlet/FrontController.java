package ituprom16.framework.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import ituprom16.framework.annotation.AnnotationController;

public class FrontController extends HttpServlet {
    private String controllerPackage;
    private List<Class<?>> controllers = new ArrayList<>();
    
    @Override
    public void init() throws ServletException {
        // Récupérer le package des contrôleurs depuis web.xml
        controllerPackage = getInitParameter("controllerPackage");
        if (controllerPackage == null || controllerPackage.isEmpty()) {
            throw new ServletException("Le package des contrôleurs n'est pas spécifié dans web.xml");
        }
        
        try {
            scanControllers();
        } catch (Exception e) {
            throw new ServletException("Erreur lors du scan des contrôleurs", e);
        }
    }
    
    private void scanControllers() throws Exception {
        String path = controllerPackage.replace('.', '/');
        URL resource = getClass().getClassLoader().getResource(path);
        if (resource == null) {
            throw new ServletException("Package " + controllerPackage + " non trouvé");
        }
        
        File directory = new File(resource.getFile());
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    String className = controllerPackage + '.' + 
                        file.getName().substring(0, file.getName().length() - 6);
                    Class<?> cls = Class.forName(className);
                    if (cls.isAnnotationPresent(AnnotationController.class)) {
                        controllers.add(cls);
                        System.out.println("Contrôleur trouvé: " + cls.getName());
                    }
                }
            }
        }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Liste des contrôleurs</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>URL appelée : " + request.getRequestURL() + "</h1>");
            out.println("<h2>Contrôleurs trouvés :</h2>");
            out.println("<ul>");
            for (Class<?> controller : controllers) {
                out.println("<li>" + controller.getName() + "</li>");
            }
            out.println("</ul>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}