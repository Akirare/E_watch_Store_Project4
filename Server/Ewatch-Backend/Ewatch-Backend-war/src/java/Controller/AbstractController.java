package Controller;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

@Named(value = "abstractController")
@SessionScoped
public class AbstractController implements Serializable
{
    public AbstractController() {}
    
    public void createSession( String sessionName , Object object )
    {
        HttpSession session = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession( false );
        
        if (session != null)
        {
            session.setAttribute( sessionName , object );
        }
    }
    
    public void destroySession ( String sessionName )
    {
        HttpSession session = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession( false );
        
        if (session != null)
        {
            session.removeAttribute( sessionName );
        }
    }
    
    public void showAlert( String message )
    {
        RequestContext.getCurrentInstance().execute( "alert(' " + message + " ');" );
    }
    
    public void reloadPage( String page )
    {
        RequestContext.getCurrentInstance().execute( "window.location.replace(' "+ page +"');" );    
    } 

}
