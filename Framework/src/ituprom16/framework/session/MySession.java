package ituprom16.framework.session;

import javax.servlet.http.HttpSession;

public class MySession {
    private HttpSession session;
    
    public MySession(HttpSession session) {
        this.session = session;
    }
    
    public void add(String key, Object value) {
        session.setAttribute(key, value);
    }
    
    public Object get(String key) {
        return session.getAttribute(key);
    }
    
    public void delete(String key) {
        session.removeAttribute(key);
    }
    
    public void invalidate() {
        session.invalidate();
    }
} 