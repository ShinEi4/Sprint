package ituprom16.framework.model;

import java.util.HashMap;

public class ModelView {
    private String url;
    private HashMap<String, Object> data;
    
    public ModelView() {
        this.data = new HashMap<>();
    }
    
    public ModelView(String url) {
        this();
        this.url = url;
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
} 