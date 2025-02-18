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
import ituprom16.framework.annotation.Param;
import java.lang.reflect.Parameter;
import ituprom16.framework.annotation.ModelAttribute;
import ituprom16.framework.annotation.RequestParam;
import java.lang.reflect.Field;
import ituprom16.framework.session.MySession;
import com.google.gson.Gson;
import ituprom16.framework.annotation.RestAPI;

public class FrontController extends HttpServlet {
    private HashMap<String, Mapping> mappingUrls;
    private String controllerPackage;
    private HashMap<String, String> errorMessages;  // Pour stocker les messages d'erreur
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        controllerPackage = config.getInitParameter("controllerPackage");
        mappingUrls = new HashMap<>();
        errorMessages = new HashMap<>();  // Initialisation des messages d'erreur
        
        if (controllerPackage == null || controllerPackage.trim().isEmpty()) {
            addError("package", "Le package des contrôleurs n'est pas spécifié dans web.xml");
            return;
        }
        scanControllers();
    }

    private void addError(String key, String message) {
        errorMessages.put(key, message);
    }
    
    private void scanControllers() throws ServletException {
        if (controllerPackage != null) {
            String packagePath = controllerPackage.replace('.', '/');
            String classPath = getServletContext().getRealPath("/WEB-INF/classes/" + packagePath);
            File packageDir = new File(classPath);
            
            if (!packageDir.exists() || !packageDir.isDirectory()) {
                throw new ServletException("Le package " + controllerPackage + " n'existe pas");
            }

            // HashMap pour vérifier les URLs en double
            HashMap<String, String> urlMethods = new HashMap<>();
            
            for (File file : packageDir.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    try {
                        String className = controllerPackage + "." + 
                            file.getName().substring(0, file.getName().length() - 6);
                        Class<?> clazz = Class.forName(className);
                        
                        if (clazz.isAnnotationPresent(AnnotationController.class)) {
                            scanMethodsWithUrlCheck(clazz, urlMethods);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new ServletException("Erreur lors du chargement de la classe: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void scanMethodsWithUrlCheck(Class<?> controller, HashMap<String, String> urlMethods) {
        String controllerName = controller.getSimpleName();
        for (Method method : controller.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GET.class)) {
                GET getAnnotation = method.getAnnotation(GET.class);
                String methodUrl = getAnnotation.value();
                String fullUrl = "/" + controllerName + methodUrl;

                // Vérifier le type de retour seulement si @RestAPI n'est pas présent
                if (!method.isAnnotationPresent(RestAPI.class)) {
                    if (!method.getReturnType().equals(String.class) && 
                        !method.getReturnType().equals(ModelView.class)) {
                        addError(controllerName + methodUrl, 
                            "Type de retour non valide pour la méthode " + method.getName() + 
                            " dans " + controllerName + ". Seuls String et ModelView sont autorisés.");
                        continue;
                    }
                }

                // Vérifier si l'URL existe déjà
                if (urlMethods.containsKey(fullUrl)) {
                    addError(fullUrl, "URL en double détectée: " + fullUrl + 
                        " dans " + controllerName + "." + method.getName() + 
                        " et " + urlMethods.get(fullUrl));
                    continue;
                }

                urlMethods.put(fullUrl, controllerName + "." + method.getName());
                mappingUrls.put(fullUrl, new Mapping(controller.getName(), method.getName()));
            }
        }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = uri.substring(contextPath.length());
        
        // Afficher les erreurs si demandé
        if (url.equals("/errors")) {
            displayErrors(response);
            return;
        }

        // Vérifier d'abord s'il y a une erreur pour cette URL
        String methodError = errorMessages.get(url);
        if (methodError != null) {
            displayError(response, methodError);
            return;
        }

        // Vérifier si une méthode est associée à l'URL
        String[] urlParts = url.split("/");
        if (urlParts.length >= 3) {
            String controllerName = urlParts[1];
            String methodPath = url.substring(url.indexOf("/", 1));
            String potentialError = controllerName + methodPath;
            
            if (errorMessages.containsKey(potentialError)) {
                displayError(response, errorMessages.get(potentialError));
                return;
            }
        }

        // Si aucune erreur n'est trouvée, continuer avec le traitement normal
        Mapping mapping = mappingUrls.get(url);
        if (mapping == null) {
            displayError(response, "Aucune méthode n'est associée à l'URL: " + url);
            return;
        }

        try {
            // Récupérer la classe par son nom
            Class<?> controllerClass = Class.forName(mapping.getClassName());
            
            // Créer une instance de la classe
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            
            // Récupérer toutes les méthodes de la classe
            Method[] methods = controllerClass.getDeclaredMethods();
            Method targetMethod = null;
            
            // Trouver la méthode avec le bon nom
            for (Method method : methods) {
                if (method.getName().equals(mapping.getMethodName())) {
                    targetMethod = method;
                    break;
                }
            }
            
            if (targetMethod == null) {
                throw new Exception("Méthode " + mapping.getMethodName() + " non trouvée");
            }
            
            // Préparer les arguments de la méthode
            Object[] methodArgs = prepareMethodArguments(targetMethod, request);
            
            // Vérifier si la méthode est annotée avec @RestAPI
            boolean isRestAPI = targetMethod.isAnnotationPresent(RestAPI.class);
            // Invoquer la méthode
            Object result = targetMethod.invoke(controllerInstance, methodArgs);
            
            if (isRestAPI) {
                // Traitement REST API
                Gson gson = new Gson();
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                
                if (result instanceof ModelView) {
                    // Si c'est un ModelView, sérialiser uniquement les données
                    ModelView mv = (ModelView) result;
                    out.println(gson.toJson(mv.getData()));
                } else {
                    // Sinon, sérialiser directement le résultat
                    out.println(gson.toJson(result));
                }
            } else {
                // Traitement normal existant
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
            }
            
        } catch (Exception e) {
            displayError(response, "Erreur: " + e.getMessage());
            e.printStackTrace(); // Pour le débogage
        }
    }

    private Object[] prepareMethodArguments(Method method, HttpServletRequest request) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            if (param.isAnnotationPresent(Param.class)) {
                args[i] = handleParamAnnotation(param, request);
            }
            else if (param.isAnnotationPresent(ModelAttribute.class)) {
                args[i] = handleModelAttribute(param.getType(), request);
            }
            else if (param.getType().equals(MySession.class)) {
                args[i] = new MySession(request.getSession());
            }
        }
        
        return args;
    }

    private Object handleParamAnnotation(Parameter param, HttpServletRequest request) {
        Param annotation = param.getAnnotation(Param.class);
        String paramName = annotation.name();
        String paramValue = request.getParameter(paramName);
        return convertParamValue(paramValue, param.getType());
    }

    private Object handleModelAttribute(Class<?> modelClass, HttpServletRequest request) {
        try {
            Object instance = modelClass.getDeclaredConstructor().newInstance();
            
            for (Field field : modelClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(RequestParam.class)) {
                    RequestParam annotation = field.getAnnotation(RequestParam.class);
                    String paramName = annotation.name();
                    if (paramName.isEmpty()) {
                        paramName = field.getName();
                    }
                    
                    String paramValue = request.getParameter(paramName);
                    if (paramValue != null) {
                        field.setAccessible(true);
                        field.set(instance, convertParamValue(paramValue, field.getType()));
                    }
                }
            }
            
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object convertParamValue(String value, Class<?> type) {
        if (value == null) return null;
        
        if (type.equals(String.class)) {
            return value;
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            return Double.parseDouble(value);
        }
        // Ajouter d'autres conversions si nécessaire
        
        return value;
    }

    private void displayError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Erreur</title>");
            out.println("<style>");
            out.println(".error { color: red; padding: 20px; border: 1px solid red; margin: 20px; }");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<div class='error'>");
            out.println("<h2>Erreur</h2>");
            out.println("<p>" + message + "</p>");
            out.println("</div>");
            out.println("</body></html>");
        }
    }

    private void displayErrors(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Erreurs Framework</title>");
            out.println("<style>");
            out.println(".error { color: red; padding: 10px; margin: 5px; border: 1px solid red; }");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<h1>Liste des erreurs détectées</h1>");
            
            if (errorMessages.isEmpty()) {
                out.println("<p>Aucune erreur détectée</p>");
            } else {
                for (Map.Entry<String, String> error : errorMessages.entrySet()) {
                    out.println("<div class='error'>");
                    out.println("<strong>" + error.getKey() + ":</strong> ");
                    out.println(error.getValue());
                    out.println("</div>");
                }
            }
            
            out.println("</body></html>");
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