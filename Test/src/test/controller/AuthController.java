package test.controller;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.annotation.GET;
import ituprom16.framework.annotation.Param;
import ituprom16.framework.model.ModelView;
import ituprom16.framework.session.MySession;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

@AnnotationController
public class AuthController {
    // Simulation d'une base de données
    private static final HashMap<String, String> USERS = new HashMap<>();
    private static final HashMap<String, List<String>> USER_DATA = new HashMap<>();
    
    static {
        // Ajout de quelques utilisateurs de test
        USERS.put("john", "pass123");
        USERS.put("alice", "pass456");
        
        // Données spécifiques aux utilisateurs
        List<String> johnData = new ArrayList<>();
        johnData.add("Projet A");
        johnData.add("Projet B");
        USER_DATA.put("john", johnData);
        
        List<String> aliceData = new ArrayList<>();
        aliceData.add("Projet X");
        aliceData.add("Projet Y");
        USER_DATA.put("alice", aliceData);
    }
    
    @GET("/login-form")
    public ModelView showLoginForm() {
        return new ModelView("/login.jsp");
    }
    
    @GET("/login")
    public ModelView login(
        @Param(name="username") String username,
        @Param(name="password") String password,
        MySession session
    ) {
        ModelView mv = new ModelView("/login.jsp");
        
        if (USERS.containsKey(username) && USERS.get(username).equals(password)) {
            session.add("user", username);
            session.add("userData", USER_DATA.get(username));
            mv.setUrl("/dashboard.jsp");
        } else {
            mv.addObject("error", "Identifiants invalides");
        }
        
        return mv;
    }
    
    @GET("/logout")
    public ModelView logout(MySession session) {
        session.invalidate();
        return new ModelView("/login.jsp");
    }
    
    @GET("/dashboard")
    public ModelView dashboard(MySession session) {
        String user = (String) session.get("user");
        if (user == null) {
            return new ModelView("/login.jsp");
        }
        
        ModelView mv = new ModelView("/dashboard.jsp");
        mv.addObject("username", user);
        mv.addObject("userProjects", session.get("userData"));
        return mv;
    }
} 