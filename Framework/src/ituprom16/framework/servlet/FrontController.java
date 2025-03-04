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
import ituprom16.framework.annotation.POST;
import javax.servlet.http.Part;
import java.io.InputStream;
import ituprom16.framework.validation.ValidationError;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import ituprom16.framework.annotation.Required;
import ituprom16.framework.annotation.Min;
import ituprom16.framework.annotation.Max;
import ituprom16.framework.annotation.Email;
import ituprom16.framework.annotation.FormUrl;
import ituprom16.framework.annotation.Auth;
import ituprom16.framework.annotation.Role;
import ituprom16.framework.annotation.Redirect;
import ituprom16.framework.model.RedirectView;
import javax.servlet.http.HttpSession;

public class FrontController extends HttpServlet {
    private HashMap<String, Mapping> mappingUrls;
    private String controllerPackage;
    private HashMap<String, String> errorMessages;  // Pour stocker les messages d'erreur
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private List<ValidationError> validationErrors;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        controllerPackage = config.getInitParameter("controllerPackage");
        mappingUrls = new HashMap<>();
        errorMessages = new HashMap<>();  // Initialisation des messages d'erreur
        validationErrors = new ArrayList<>();
        
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
            String methodUrl = null;
            String httpMethod = null;

            // Vérifier GET
            if (method.isAnnotationPresent(GET.class)) {
                GET getAnnotation = method.getAnnotation(GET.class);
                methodUrl = getAnnotation.value();
                httpMethod = "GET";
            }
            // Vérifier POST
            else if (method.isAnnotationPresent(POST.class)) {
                POST postAnnotation = method.getAnnotation(POST.class);
                methodUrl = postAnnotation.value();
                httpMethod = "POST";
            }
            // Méthode sans annotation = GET par défaut
            else {
                methodUrl = "/" + method.getName();
                httpMethod = "GET";
            }

            if (methodUrl != null) {
                String fullUrl = "/" + controllerName + methodUrl;

                // Vérifier si l'URL existe déjà avec la même méthode HTTP
                String existingMapping = urlMethods.get(fullUrl);
                if (existingMapping != null) {
                    addError(fullUrl, "URL en double détectée: " + fullUrl + 
                        " dans " + controllerName + "." + method.getName() + 
                        " et " + existingMapping);
                    continue;
                }

                urlMethods.put(fullUrl, controllerName + "." + method.getName());
                mappingUrls.put(fullUrl, new Mapping(controller.getName(), method.getName(), httpMethod));
            }
        }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        validationErrors.clear();
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = uri.substring(contextPath.length());
        
        // Utiliser la méthode stockée dans l'attribut si elle existe (pour les redirections)
        String requestMethod = (String) request.getAttribute("method");
        if (requestMethod == null) {
            requestMethod = request.getMethod();
        }

        // Récupérer le mapping
        Mapping mapping = mappingUrls.get(url);
        if (mapping == null) {
            displayError(response, "Aucune méthode n'est associée à l'URL: " + url);
            return;
        }

        // Pour les redirections, on accepte toujours GET même si la méthode d'origine était POST
        if (!mapping.getHttpMethod().equals(requestMethod) && !"GET".equals(mapping.getHttpMethod())) {
            displayError(response, "Méthode HTTP incorrecte. L'URL " + url + 
                " attend " + mapping.getHttpMethod() + " mais a reçu " + requestMethod);
            return;
        }

        try {
            // Récupérer la classe et la méthode
            Class<?> controllerClass = Class.forName(mapping.getClassName());
            Method targetMethod = null;
            
            // Trouver la méthode avec le bon nom
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.getName().equals(mapping.getMethodName())) {
                    targetMethod = method;
                    break;
                }
            }
            
            if (targetMethod == null) {
                throw new Exception("Méthode " + mapping.getMethodName() + " non trouvée");
            }
            
            // Préparer les arguments et valider
            Object[] args = prepareMethodArguments(targetMethod, request);
            
            // Vérifier s'il y a des erreurs de validation
            if (!validationErrors.isEmpty()) {
                request.setAttribute("validationErrors", validationErrors);
                // Vérifier si la méthode a une annotation FormUrl
                if (targetMethod.isAnnotationPresent(FormUrl.class)) {
                    String formPath = targetMethod.getAnnotation(FormUrl.class).value();
                    System.out.println("Chemin du formulaire: " + formPath);
                    
                    // Créer une nouvelle requête GET vers le formulaire
                    request.setAttribute("method", "GET");
                    Mapping formMapping = mappingUrls.get(formPath);
                    if (formMapping != null) {
                        try {
                            Class<?> formControllerClass = Class.forName(formMapping.getClassName());
                            Method formMethod = null;
                            for (Method method : formControllerClass.getDeclaredMethods()) {
                                if (method.getName().equals(formMapping.getMethodName())) {
                                    formMethod = method;
                                    break;
                                }
                            }
                            if (formMethod != null) {
                                Object formControllerInstance = formControllerClass.getDeclaredConstructor().newInstance();
                                Object formResult = formMethod.invoke(formControllerInstance);
                                handleMethodResult(formResult, request, response, formMethod);
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            displayValidationErrors(response);
                        }
                    } else {
                        displayValidationErrors(response);
                    }
                } else {
                    displayValidationErrors(response);
                }
                return;
            }
            
            // Vérifier l'authentification
            checkAuthentication(request, response, controllerClass, targetMethod);

            // Vérifier les rôles
            if (!checkRole(request, controllerClass, targetMethod)) {
                displayError(response, "Accès refusé : vous n'avez pas les droits nécessaires");
                return;
            }
            
            // Si pas d'erreur, exécuter la méthode
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            Object result = targetMethod.invoke(controllerInstance, args);
            
            // Traiter le résultat...
            handleMethodResult(result, request, response, targetMethod);

        } catch (Exception e) {
            e.printStackTrace();
            displayError(response, "Erreur: " + e.getMessage());
        }
    }

    private Object[] prepareMethodArguments(Method method, HttpServletRequest request) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            try {
                if (param.isAnnotationPresent(Param.class)) {
                    args[i] = handleParamAnnotation(param, request);
                }
                else if (param.isAnnotationPresent(ModelAttribute.class)) {
                    args[i] = handleModelAttribute(param.getType(), request);
                }
                else if (param.getType().equals(MySession.class)) {
                    args[i] = new MySession(request.getSession());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return args;
    }

    private Object handleParamAnnotation(Parameter param, HttpServletRequest request) {
        Param annotation = param.getAnnotation(Param.class);
        String paramName = annotation.name();
        String paramValue = request.getParameter(paramName);
        
        // Vérifier d'abord si le champ est requis
        if (param.isAnnotationPresent(Required.class)) {
            Required required = param.getAnnotation(Required.class);
            if (paramValue == null || paramValue.trim().isEmpty()) {
                validationErrors.add(new ValidationError(paramName, required.message()));
                return null;
            }
        }

        // Vérifier si c'est un fichier (multipart)
        if (param.getType().equals(byte[].class) || 
            (param.getType().equals(String.class) && request.getContentType() != null && 
             request.getContentType().startsWith("multipart/form-data"))) {
            try {
                Part filePart = request.getPart(paramName);
                if (filePart != null) {
                    // Si le paramètre attend un tableau de bytes
                    if (param.getType().equals(byte[].class)) {
                        InputStream inputStream = filePart.getInputStream();
                        return inputStream.readAllBytes();
                    }
                    // Si le paramètre attend le nom du fichier
                    else if (param.getType().equals(String.class)) {
                        return filePart.getSubmittedFileName();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // Convertir la valeur
        Object value = convertParamValue(paramValue, param.getType());
        
        // Valider le champ
        validateField(paramName, value, param);
        
        return value;
    }

    private Object handleModelAttribute(Class<?> modelClass, HttpServletRequest request) {
        try {
            Object instance = modelClass.getDeclaredConstructor().newInstance();
            Field[] fields = modelClass.getDeclaredFields();
            
            for (Field field : fields) {
                field.setAccessible(true);
                String paramValue = request.getParameter(field.getName());
                
                if (paramValue != null && !paramValue.trim().isEmpty()) {
                    // Conversion des types
                    Object convertedValue = convertParamValue(paramValue, field.getType());
                    field.set(instance, convertedValue);
                }
            }
            
            // Valider le modèle après avoir défini toutes les valeurs
            validateModel(instance);
            
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object convertParamValue(String value, Class<?> type) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Conversion String
            if (type.equals(String.class)) {
                return value;
            }
            
            // Conversion Integer/int
            if (type.equals(Integer.class) || type.equals(int.class)) {
                return Integer.parseInt(value.trim());
            }
            
            // Conversion Double/double
            if (type.equals(Double.class) || type.equals(double.class)) {
                return Double.parseDouble(value.trim());
            }
            
            // Conversion java.sql.Date
            if (type.equals(java.sql.Date.class)) {
                try {
                    // Format attendu: YYYY-MM-DD
                    return java.sql.Date.valueOf(value.trim());
                } catch (IllegalArgumentException e) {
                    validationErrors.add(new ValidationError(
                        "date", 
                        "Le format de date doit être YYYY-MM-DD"
                    ));
                    return null;
                }
            }
            
            // Conversion java.sql.Timestamp
            if (type.equals(java.sql.Timestamp.class)) {
                try {
                    // Format attendu: YYYY-MM-DD HH:mm:ss ou YYYY-MM-DD HH:mm:ss.SSS
                    if (value.length() <= 19) {
                        // Si pas de millisecondes, ajouter .0
                        value = value.trim() + ".0";
                    }
                    return java.sql.Timestamp.valueOf(value.trim());
                } catch (IllegalArgumentException e) {
                    validationErrors.add(new ValidationError(
                        "timestamp", 
                        "Le format de date/heure doit être YYYY-MM-DD HH:mm:ss"
                    ));
                    return null;
                }
            }
            
            // Si le type n'est pas géré, retourner la valeur brute
            return value;
            
        } catch (NumberFormatException e) {
            // Erreur de conversion numérique
            validationErrors.add(new ValidationError(
                "conversion", 
                "Erreur de conversion pour la valeur: " + value
            ));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void validateField(String fieldName, Object value, Parameter param) {
        if (value != null) {
            // Validation Min
            if (param.isAnnotationPresent(Min.class)) {
                Min min = param.getAnnotation(Min.class);
                double numValue = Double.parseDouble(value.toString());
                if (numValue < min.value()) {
                    validationErrors.add(new ValidationError(fieldName, 
                        min.message().replace("{value}", String.valueOf(min.value()))));
                }
            }
            
            // Validation Max
            if (param.isAnnotationPresent(Max.class)) {
                Max max = param.getAnnotation(Max.class);
                double numValue = Double.parseDouble(value.toString());
                if (numValue > max.value()) {
                    validationErrors.add(new ValidationError(fieldName, 
                        max.message().replace("{value}", String.valueOf(max.value()))));
                }
            }
            
            // Validation Email
            if (param.isAnnotationPresent(Email.class)) {
                Email email = param.getAnnotation(Email.class);
                if (!EMAIL_PATTERN.matcher(value.toString()).matches()) {
                    validationErrors.add(new ValidationError(fieldName, email.message()));
                }
            }
        }
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

    private void displayValidationErrors(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Erreurs de validation</title>");
            out.println("<style>");
            out.println(".error-container { margin: 20px; padding: 20px; border: 2px solid red; }");
            out.println(".error-title { color: red; font-size: 1.2em; margin-bottom: 10px; }");
            out.println(".error-item { color: #d00; margin: 5px 0; padding: 5px; background: #fee; }");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<div class='error-container'>");
            out.println("<div class='error-title'>Erreurs de validation :</div>");
            for (ValidationError error : validationErrors) {
                out.println("<div class='error-item'>");
                out.println( error.getField() + " : " + error.getMessage());
                out.println("</div>");
            }
            out.println("</div>");
            out.println("</body></html>");
        }
    }

    private void handleMethodResult(Object result, HttpServletRequest request, HttpServletResponse response, Method targetMethod) 
            throws ServletException, IOException {
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
            ModelView modelView = (ModelView) result;
            
            // Ajouter les attributs à la requête
            for (Map.Entry<String, Object> entry : modelView.getData().entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
            
            // Vérifier s'il y a des erreurs de validation
            if (!validationErrors.isEmpty()) {
                request.setAttribute("validationErrors", validationErrors);
                
                // Si c'est une redirection avec RedirectView
                if (result instanceof RedirectView) {
                    RedirectView redirectView = (RedirectView) result;
                    
                    // Vérifier s'il y a des erreurs de validation
                    if (!validationErrors.isEmpty()) {
                        request.setAttribute("validationErrors", validationErrors);
                        // Forcer la méthode GET pour la redirection d'erreur
                        request.setAttribute("method", "GET");
                        response.sendRedirect(request.getContextPath() + redirectView.getErrorRedirectUrl());
                        return;
                    }
                    
                    // Stocker les données dans la session si useSession est true
                    if (redirectView.isUseSession()) {
                        HttpSession session = request.getSession();
                        for (Map.Entry<String, Object> entry : redirectView.getData().entrySet()) {
                            session.setAttribute(entry.getKey(), entry.getValue());
                        }
                    }
                    
                    // Forcer la méthode GET pour la redirection de succès
                    request.setAttribute("method", "GET");
                    response.sendRedirect(request.getContextPath() + redirectView.getUrl());
                    return;
                }
            }
            
            // Forward normal
            RequestDispatcher dispatcher = request.getRequestDispatcher(modelView.getUrl());
            dispatcher.forward(request, response);
        }
        else if (targetMethod.isAnnotationPresent(RestAPI.class)) {
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
        }
        else {
            // Type non reconnu
            displayError(response, "Type de retour non reconnu");
        }
    }

    private void validateModel(Object model) {
        if (model == null) return;
        
        Class<?> modelClass = model.getClass();
        Field[] fields = modelClass.getDeclaredFields();
        
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(model);
                
                // Validation Required
                if (field.isAnnotationPresent(Required.class)) {
                    Required required = field.getAnnotation(Required.class);
                    if (value == null || (value instanceof String && ((String)value).trim().isEmpty())) {
                        validationErrors.add(new ValidationError(field.getName(), required.message()));
                        continue;
                    }
                }
                
                if (value != null) {
                    // Validation Email
                    if (field.isAnnotationPresent(Email.class)) {
                        Email email = field.getAnnotation(Email.class);
                        if (!EMAIL_PATTERN.matcher(value.toString()).matches()) {
                            validationErrors.add(new ValidationError(field.getName(), email.message()));
                        }
                    }
                    
                    // Validation Min
                    if (field.isAnnotationPresent(Min.class)) {
                        Min min = field.getAnnotation(Min.class);
                        double numValue = Double.parseDouble(value.toString());
                        if (numValue < min.value()) {
                            validationErrors.add(new ValidationError(field.getName(), min.message()));
                        }
                    }
                    
                    // Validation Max
                    if (field.isAnnotationPresent(Max.class)) {
                        Max max = field.getAnnotation(Max.class);
                        double numValue = Double.parseDouble(value.toString());
                        if (numValue > max.value()) {
                            validationErrors.add(new ValidationError(field.getName(), max.message()));
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
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

    private boolean checkAuthentication(HttpServletRequest request, HttpServletResponse response, Class<?> controllerClass, Method method) throws Exception {
        // Vérifier l'annotation au niveau de la classe
        boolean requiresAuth = controllerClass.isAnnotationPresent(Auth.class);
        String loginUrl = "/auth/test"; // URL par défaut
        
        // L'annotation de méthode surcharge celle de la classe
        if (method.isAnnotationPresent(Auth.class)) {
            requiresAuth = method.getAnnotation(Auth.class).required();
            loginUrl = method.getAnnotation(Auth.class).loginUrl();
        } else if (controllerClass.isAnnotationPresent(Auth.class)) {
            loginUrl = controllerClass.getAnnotation(Auth.class).loginUrl();
        }
        
        if (requiresAuth) {
            MySession session = new MySession(request.getSession());
            if (session.get("user") == null) {
                try {
                    response.sendRedirect(request.getContextPath() + loginUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
            return true;
        }
        return true;
    }

    private boolean checkRole(HttpServletRequest request, Class<?> controllerClass, Method method) {
        String[] requiredRoles = null;
        
        // Vérifier l'annotation au niveau de la classe
        if (controllerClass.isAnnotationPresent(Role.class)) {
            requiredRoles = controllerClass.getAnnotation(Role.class).value();
        }
        
        // L'annotation de méthode surcharge celle de la classe
        if (method.isAnnotationPresent(Role.class)) {
            requiredRoles = method.getAnnotation(Role.class).value();
        }
        
        if (requiredRoles != null && requiredRoles.length > 0) {
            MySession session = new MySession(request.getSession());
            String[] userRoles = (String[]) session.get("roles");
            if (userRoles == null) return false;
            
            // Vérifier si l'utilisateur a au moins un des rôles requis
            for (String required : requiredRoles) {
                for (String userRole : userRoles) {
                    if (required.equals(userRole)) return true;
                }
            }
            return false;
        }
        return true;
    }
} 