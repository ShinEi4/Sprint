package test.controller;

import ituprom16.framework.annotation.AnnotationController;
import ituprom16.framework.annotation.GET;
import ituprom16.framework.annotation.POST;
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
    private static final HashMap<String, String[]> USER_ROLES = new HashMap<>();
    
    static {
        // Ajout de quelques utilisateurs de test
        USERS.put("john", "pass123");
        USERS.put("alice", "pass456");
        USERS.put("bob", "pass789");
        
        // Attribution des rôles
        USER_ROLES.put("john", new String[]{"ADMIN", "USER_MANAGER"});
        USER_ROLES.put("alice", new String[]{"USER_MANAGER"});
        USER_ROLES.put("bob", new String[]{"USER"});
    }
    
    @GET("/login-form")
    public ModelView showLoginForm() {
        return new ModelView("/login.jsp");
    }

    @GET("/index")
    public ModelView showIndex() {
        return new ModelView("/admin/index.jsp");
    }

    @POST("/login")
    public ModelView login(
        @Param(name="username") String username,
        @Param(name="password") String password,
        MySession session
    ) {
        ModelView mv = new ModelView("/login.jsp");
        
        if (USERS.containsKey(username) && USERS.get(username).equals(password)) {
            session.add("user", username);
            session.add("roles", USER_ROLES.get(username));
            mv.setUrl("/admin/index.jsp");
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
} 