package Controller;

import DAO.EmployeesFacadeLocal;
import Entity.Employees;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named(value = "LoginController")
@SessionScoped
public class LoginController extends AbstractController implements Serializable
{

    @EJB
    private EmployeesFacadeLocal employeesFacade;
    private String UserName;
    private String UserPassword;
    private String url;
    
    public LoginController() {}
    
    public String login()
    {  
        if ( checkLogin( UserName , UserPassword ) )
        {   
            //super.showAlert( "Login Succussful." );
            
            this.url = "admin_Employee_List?faces-redirect=true";
        }
        else
        {
            super.showAlert( "Username or Password is not correct." );
            
            this.url = "admin_Login.xhtml?faces-redirect=true";
        }
        
        return this.url;
    }
    
    public String logout()
    {
        super.destroySession( "CurrentEmployee" );
        
        this.url = "admin_Login?faces-redirect=true";
        
        return this.url;
    }
    
    public boolean checkLogin( String username , String password )
    {
        boolean         accountComfirm  = false;
        List<Employees> employeesList   = this.employeesFacade.findAll();
        
        for( Employees currentElm : employeesList )
        {
            if( currentElm.getUserName().equals( username ) &&  currentElm.getUserPassword().equals( password ) )
            {
                accountComfirm = true;
                
                super.createSession( "CurrentEmployee" , currentElm );
                
                break;
            }
            else
            {
                accountComfirm = false;
            }  
        }
        
        return accountComfirm;
    }
    
    public String getUserName() { return UserName; }
    public void setUserName(String UserName) { this.UserName = UserName; }
    public String getUserPassword() { return UserPassword; }
    public void setUserPassword(String UserPassword) { this.UserPassword = UserPassword; }
}
