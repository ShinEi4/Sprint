package ituprom16.framework.servlet;

import ituprom16.framework.annotation.AnnotationController;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import javax.servlet.ServletConfig;

public class FrontController extends HttpServlet {
    private List<String> controllerNames;
    private String controllerPackage;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        controllerPackage = config.getInitParameter("controllerPackage");
        controllerNames = new ArrayList<>();
        scanControllers();
    }
    
    private void scanControllers() {
        if (controllerNames.isEmpty() && controllerPackage != null) {
            String packagePath = controllerPackage.replace('.', '/');
            String classPath = getServletContext().getRealPath("/WEB-INF/classes/" + packagePath);
            File packageDir = new File(classPath);
            
            if (packageDir.exists() && packageDir.isDirectory()) {
                for (File file : packageDir.listFiles()) {
                    if (file.getName().endsWith(".class")) {
                        try {
                            String className = controllerPackage + "." + 
                                file.getName().substring(0, file.getName().length() - 6);
                            Class<?> clazz = Class.forName(className);
                            
                            if (clazz.isAnnotationPresent(AnnotationController.class)) {
                                controllerNames.add(clazz.getSimpleName());
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
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
            for (String controllerName : controllerNames) {
                out.println("<li>" + controllerName + "</li>");
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