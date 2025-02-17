package ituprom16.framework.servlet;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.annotation.GET;
import ituprom16.framework.model.Mapping;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.io.File;
import javax.servlet.ServletConfig;

public class FrontController extends HttpServlet {
    private HashMap<String, Mapping> mappingUrls;
    private String controllerPackage;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        controllerPackage = config.getInitParameter("controllerPackage");
        mappingUrls = new HashMap<>();
        scanControllers();
    }
    
    private void scanControllers() {
        if (mappingUrls.isEmpty() && controllerPackage != null) {
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
                                scanMethods(clazz);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void scanMethods(Class<?> controller) {
        String controllerName = controller.getSimpleName();
        for (Method method : controller.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GET.class)) {
                GET getAnnotation = method.getAnnotation(GET.class);
                String methodUrl = getAnnotation.value();
                String fullUrl = "/" + controllerName + methodUrl;
                Mapping mapping = new Mapping(controller.getName(), method.getName());
                mappingUrls.put(fullUrl, mapping);
            }
        }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = uri.substring(contextPath.length());
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Résultat de la méthode</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>URL appelée : " + url + "</h1>");
            
            Mapping mapping = mappingUrls.get(url);
            if (mapping != null) {
                try {
                    // Récupérer la classe par son nom
                    Class<?> controllerClass = Class.forName(mapping.getClassName());
                    
                    // Créer une instance de la classe
                    Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                    
                    // Récupérer la méthode par son nom
                    Method method = controllerClass.getDeclaredMethod(mapping.getMethodName());
                    
                    // Invoquer la méthode sur l'instance
                    String result = (String) method.invoke(controllerInstance);
                    
                    // Afficher le résultat
                    out.println("<h2>Résultat de la méthode :</h2>");
                    out.println("<p>" + result + "</p>");
                    
                } catch (Exception e) {
                    out.println("<p>Erreur lors de l'exécution de la méthode : " + e.getMessage() + "</p>");
                    e.printStackTrace();
                }
            } else {
                out.println("<p>Aucune méthode n'est associée à ce chemin.</p>");
            }
            
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