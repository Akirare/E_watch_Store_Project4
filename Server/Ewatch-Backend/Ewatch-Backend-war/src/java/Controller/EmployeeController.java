package Controller;

import DAO.EmployeesFacadeLocal;
import Entity.Employees;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named( value = "EmployeeController" )
@SessionScoped
public class EmployeeController extends AbstractController implements Serializable
{

    @EJB
    private EmployeesFacadeLocal    facade;
    private Employees               employee;
    private String                  userName;
    private String                  userPassword;
    private String                  userRole;
    private String                  searchEmployeeByEmployeeUsername = "";
    private List<Employees>         employeesList;
    private String                  validationMessage;       
    private String                  url;
    
    public List<Employees> findAll()
    {
        if( "".equals( this.searchEmployeeByEmployeeUsername ) )
        {
            employeesList = this.facade.findAll();
        }
        else 
        {
            employeesList = findAllEmployeesByEmployeeUsername();
        }
        
        return employeesList;
    }
    
    public List<Employees> findAllEmployeesByEmployeeUsername()
    {      
        List<Employees> resultList = new ArrayList<>();
        
        for( Employees currentEmployee : this.facade.findAll() )
        {
            if( currentEmployee.getUserName().contains( this.searchEmployeeByEmployeeUsername ) )
            {
                resultList.add(currentEmployee );
            }
        }
        
        return resultList;
    }
    
    public String create()
    {
        this.validationMessage = checkEmployeeField();
           
        if ( "Success".equals( this.validationMessage ) )
        {
           this.validationMessage = checkUsernameExisted();
           
           if ( "Success".equals( this.validationMessage ) )
           {
               Employees handlingEmployeeAccount = new Employees( userName , userPassword , userRole );
               
               this.facade.create( handlingEmployeeAccount );
               
               this.url = "admin_Employee_List.xhtml?faces-redirect=true";
           }
           else
           {
               this.url = "admin_Employee_Create.xhtml?faces-redirect=true";
           }
        }
        else
        {
            this.url = "admin_Employee_Create.xhtml?faces-redirect=true";
        }
            
        super.showAlert( this.validationMessage );
        
        return this.url;
    }
    
    public String update()
    {
        this.validationMessage = checkEmployeeField();
        
        if( "Success".equals( this.validationMessage ) )
        {
            Employees handlingEmployeeAccount = new Employees( this.userName , this.userPassword , this.userRole );
               
            this.facade.edit( handlingEmployeeAccount );
               
            this.url = "admin_Employee_List.xhtml?faces-redirect=true";
        }
        else
        {
            this.url = "admin_Employee_Update.xhtml?faces-redirect=true";
        }
        
        super.showAlert( this.validationMessage );

        return this.url;
    }
    
    public String remove( Employees currentEmployee )
    {
        this.facade.remove( currentEmployee );
        
        this.url = "admin_Employee_List.xhtml?faces-redirect=true";
        
        super.showAlert( "Remove succesful" );
        
        return this.url;
    }
    
    public String redirect( Employees employee )
    {
        this.userName       =   employee.getUserName();
        this.userPassword   =   employee.getUserPassword();
        this.userRole       =   employee.getUserRole();
        
        this.url            =   "admin_Employee_Update?faces-redirect=true";
        
        return this.url;
    }
    
    public String checkUsernameExisted()
    {
        String localMessage = "Success";
        
        for ( Employees currentEmployee : this.facade.findAll() )
        {
            if ( Objects.equals( currentEmployee.getUserName() , this.userName ) )
            {
                localMessage = "This username has already existed";
                
                break;
            }
        }
        
        return localMessage;
    }
    
    public String checkEmployeeField()
    {
        String localMessage;
        
        if ( !this.userName.isEmpty() && this.userName != null )
        {
            if ( !this.userPassword.isEmpty() && this.userPassword != null )
            {
                if( this.userName.matches( "^[A-z]([A-z0-9\\S]{5,20})" ) )
                {
                    if( this.userPassword.matches( "^[A-z]([A-z0-9\\S]{5,20})" ) ) 
                    {
                        localMessage = "Success";
                    }
                    else
                    {
                        localMessage = "Password field must start with one character, must contain no special characters, no spaces, have at least 6 characters and maximum 20 characters.";
                    }
                }
                else
                {
                    localMessage = "Username field must start with a character, must contain no special characters, no spaces, have at least 6 characters and maximum 20 characters.";
                }  
            }
            else 
            { 
                localMessage = "Password field cannot be empty.";
            }
        }
        else
        {
            localMessage = "Username field cannot be empty.";
        }
        
        return localMessage;
    }
    
    public EmployeeController() {}
    public Employees getEmployee() { return employee; }
    public void setEmployee(Employees employee) { this.employee = employee; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public String getSearchEmployeeByEmployeeUsername() { return searchEmployeeByEmployeeUsername; }
    public void setSearchEmployeeByEmployeeUsername(String searchEmployeeByEmployeeUsername) { this.searchEmployeeByEmployeeUsername = searchEmployeeByEmployeeUsername; }
}
