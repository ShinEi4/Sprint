package test.controller;

import ituprom16.framework.annotation.*;
import ituprom16.framework.model.ModelView;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

@AnnotationController
public class AdminController {
    
    // Simulation d'une base de données
    private static final List<String> USERS = new ArrayList<>();
    private static final HashMap<String, String[]> USER_ROLES = new HashMap<>();
    
    static {
        // Ajout des utilisateurs de test
        USERS.add("john");
        USERS.add("alice");
        USERS.add("bob");
        
        // Attribution des rôles
        USER_ROLES.put("john", new String[]{"ADMIN", "USER_MANAGER"});
        USER_ROLES.put("alice", new String[]{"USER_MANAGER"});
        USER_ROLES.put("bob", new String[]{"USER"});
    }
    
    // Test 1: Accessible uniquement aux ADMIN (hérité de la classe)
    @GET("/all")
    public ModelView getAllUsers() {
        ModelView mv = new ModelView("/admin/users.jsp");
        mv.addObject("users", USERS);
        mv.addObject("title", "Liste des utilisateurs (ADMIN uniquement)");
        return mv;
    }
    
    // Test 2: Accessible uniquement aux ADMIN (hérité de la classe)
    @GET("/count")
    public ModelView getUserCount() {
        ModelView mv = new ModelView("/admin/count.jsp");
        mv.addObject("count", USERS.size());
        mv.addObject("title", "Nombre d'utilisateurs (ADMIN uniquement)");
        return mv;
    }
    
    // Test 3: Accessible aux ADMIN et USER_MANAGER (surcharge de la classe)
    @GET("/details")
    @Role({"ADMIN", "USER_MANAGER"})
    public ModelView getUserDetails() {
        ModelView mv = new ModelView("/admin/details.jsp");
        mv.addObject("users", USERS);
        mv.addObject("roles", USER_ROLES);
        mv.addObject("title", "Détails des utilisateurs (ADMIN ou USER_MANAGER)");
        return mv;
    }
    
    // Test 4: Désactivation de l'authentification pour cette méthode
    @GET("/public-info")
    @Auth(required = false)
    public ModelView getPublicInfo() {
        ModelView mv = new ModelView("/admin/public.jsp");
        mv.addObject("title", "Informations publiques");
        mv.addObject("message", "Cette page est accessible à tous");
        return mv;
    }
    
    // Test 5: Accessible uniquement aux USER_MANAGER (surcharge complète)
    @GET("/manager-only")
    @Role({"USER_MANAGER"})
    public ModelView getManagerOnlyInfo() {
        ModelView mv = new ModelView("/admin/manager.jsp");
        mv.addObject("title", "Page des managers");
        mv.addObject("message", "Cette page est accessible uniquement aux USER_MANAGER");
        return mv;
    }
    
    // Test 6: Accessible à tous les utilisateurs authentifiés
    @GET("/authenticated-only")
    @Role({})  // Pas de rôle requis, mais authentification nécessaire
    public ModelView getAuthenticatedOnlyInfo() {
        ModelView mv = new ModelView("/admin/authenticated.jsp");
        mv.addObject("title", "Page authentifiée");
        mv.addObject("message", "Cette page est accessible à tous les utilisateurs connectés");
        return mv;
    }
} 