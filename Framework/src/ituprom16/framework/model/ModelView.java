package ituprom16.framework.model;

import java.util.HashMap;

public class ModelView {
    private String url;
    private HashMap<String, Object> data;
    private boolean isRedirect;
    
    public ModelView() {
        this.data = new HashMap<>();
        this.isRedirect = false;
    }
    
    public ModelView(String url) {
        this();
        this.url = url;
    }
    
    public ModelView(String url, boolean isRedirect) {
        this();
        this.url = url;
        this.isRedirect = isRedirect;
    }
    
    public void addObject(String name, Object value) {
        this.data.put(name, value);
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public HashMap<String, Object> getData() {
        return data;
    }
    
    public boolean isRedirect() {
        return isRedirect;
    }
    
    public void setRedirect(boolean redirect) {
        isRedirect = redirect;
    }
} 