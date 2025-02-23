package ituprom16.framework.model;

public class RedirectView extends ModelView {
    private String errorRedirectUrl;
    private boolean useSession = true;  // Par défaut, on utilise la session
    
    public RedirectView(String successUrl, String errorUrl) {
        super(successUrl, true);  // true pour indiquer que c'est une redirection
        this.errorRedirectUrl = errorUrl;
    }
    
    public String getErrorRedirectUrl() {
        return errorRedirectUrl;
    }
    
    public void setErrorRedirectUrl(String errorRedirectUrl) {
        this.errorRedirectUrl = errorRedirectUrl;
    }
    
    public boolean isUseSession() {
        return useSession;
    }
    
    public void setUseSession(boolean useSession) {
        this.useSession = useSession;
    }
} 