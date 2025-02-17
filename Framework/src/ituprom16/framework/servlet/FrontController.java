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
import ituprom16.framework.model.ModelView;
import javax.servlet.RequestDispatcher;
import java.util.Map;

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
                Object result = method.invoke(controllerInstance);
                
                // Traiter le résultat selon son type
                if (result instanceof String) {
                    // Si c'est une String, l'afficher directement
                    response.setContentType("text/html;charset=UTF-8");
                    try (PrintWriter out = response.getWriter()) {
                        out.println("<!DOCTYPE html>");
                        out.println("<html><head><title>Résultat</title></head><body>");
                        out.println(result);
                        out.println("</body></html>");
                    }
                } 
                else if (result instanceof ModelView) {
                    // Si c'est un ModelView, dispatcher vers l'URL spécifiée
                    ModelView modelView = (ModelView) result;
                    
                    // Ajouter les données dans la requête
                    for (Map.Entry<String, Object> entry : modelView.getData().entrySet()) {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }
                    
                    // Dispatcher vers l'URL
                    RequestDispatcher dispatcher = request.getRequestDispatcher(modelView.getUrl());
                    dispatcher.forward(request, response);
                }
                else {
                    // Type non reconnu
                    response.setContentType("text/html;charset=UTF-8");
                    try (PrintWriter out = response.getWriter()) {
                        out.println("<!DOCTYPE html>");
                        out.println("<html><head><title>Erreur</title></head><body>");
                        out.println("<p>Type de retour non reconnu</p>");
                        out.println("</body></html>");
                    }
                }
                
            } catch (Exception e) {
                throw new ServletException(e);
            }
        } else {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html><head><title>Erreur</title></head><body>");
                out.println("<p>Aucune méthode n'est associée à ce chemin.</p>");
                out.println("</body></html>");
            }
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