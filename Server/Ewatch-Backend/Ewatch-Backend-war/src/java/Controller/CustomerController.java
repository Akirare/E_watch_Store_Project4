package Controller;

import DAO.CustomersFacadeLocal;
import Entity.Customers;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named(value = "CustomerController")
@SessionScoped
public class CustomerController extends AbstractController implements Serializable
{
    @EJB
    private CustomersFacadeLocal    customerFacade;
    private Customers               customer;
    private String                  username;
    private String                  password;
    private String                  fullname;
    private String                  phone;
    private String                  address;
    private String                  status;
    private String                  searchCustomertByUsername = "";
    private List<Customers>         customersList;
    private String                  validationMessage;
    private String                  url;
    
    public List<Customers> findAll()
    {
        if( "".equals( this.searchCustomertByUsername ) )
        {
            customersList = this.customerFacade.findAll();
        }
        else 
        {
            customersList = findAllCustomerssByCustomerUsername();
        }
        
        return customersList;
    }
    
    public List<Customers> findAllCustomerssByCustomerUsername()
    {      
        List<Customers> resultList = new ArrayList<>();
        
        for( Customers currentCustomer : this.customerFacade.findAll() )
        {
            if( currentCustomer.getCustomerId().contains(this.searchCustomertByUsername ) )
            {
                resultList.add( currentCustomer );
            }
        }
        return resultList;
    }
    
    public String getCustomerStatusString( String status ) 
    { 
        if( status.equals("0") )
        {
            return "Disable";
        }
        else
        {
            return "Active";
        }
    }
    
    public String create()
    {
        this.validationMessage = this.checkCustomerField();
        
        if ( this.validationMessage.equals( "Success" ) )
        {
            this.validationMessage = this.checkCustomerUsernameExisted();
            
            if ( this.validationMessage.equals( "Success" ) )
            {
                Customers handlingCustomerAccount = new Customers( this.username , this.password , this.fullname , this.address , this.phone , "1" );
                
                this.customerFacade.create( handlingCustomerAccount );
                
                this.url = "admin_Customer_List.xhtml?faces-redirect=true";
            }
            else
            {   
                this.url = "admin_Customer_Create.xhtml?faces-redirect=true";
            }  
        }
        else
        {
            this.url = "admin_Customer_Create.xhtml?faces-redirect=true";
        }
        
        super.showAlert( this.validationMessage );
        
        return this.url;
    }
    
    public String update()
    {    
        this.validationMessage  = this.checkCustomerField();        
        
        if ( this.validationMessage.equals( "Success" ) )
        {
            Customers handlingCustomerAccount = new Customers( this.username , this.password , this.fullname , this.address , this.phone , this.status );
            
            this.customerFacade.edit( handlingCustomerAccount );
            
            this.url = "admin_Customer_List.xhtml?faces-redirect=true";
        }
        else
        {
            this.url = "admin_Customer_Update.xhtml?faces-redirect=true";
        }
        
        super.showAlert( this.validationMessage );

        return this.url;
    }
    
    public String remove( Customers currentCustomer )
    {
        this.customerFacade.remove( currentCustomer );
        
        this.url = "admin_Customer_List.xhtml?faces-redirect=true";
        
        super.showAlert( "Remove succesful" );
        
        return this.url;
    }
    
    public String redirect( Customers customer )
    {
        this.username   =   customer.getCustomerId();
        this.password   =   customer.getCustomerPassword();
        this.fullname   =   customer.getCustomerName();
        this.phone      =   customer.getPhoneNo();
        this.address    =   customer.getCustomerAddress();
        this.status     =   customer.getCustomerStatus();
        
        this.url        =   "admin_Customer_Update?faces-redirect=true";
        
        return this.url;
    }
    
    public String checkCustomerUsernameExisted()
    {
        String localMessage = "Success";
        
        for( Customers currentCustomer : this.customerFacade.findAll() )
        {
            if( Objects.equals( currentCustomer.getCustomerId() , this.username ) )
            {
                localMessage = "This username has already existed";
                
                break;
            }
        }
        
        return localMessage;
    }
    
    public String checkCustomerField()
    {
        String localMessage;
        
        if ( this.username != null && !this.username.isEmpty() )
        {
            if ( this.password != null && !this.password.isEmpty() )
            {
                if ( this.fullname != null && !this.fullname.isEmpty() )
                {
                    if ( this.phone != null && !this.phone.isEmpty() )
                    {
                        if( this.address != null && !this.address.isEmpty() )
                        {
                            if( this.username.matches( "^[A-z]([A-z0-9\\S]{5,20})" ) )
                            {
                                if( this.password.matches( "^[A-z]([A-z0-9\\S]{5,20})" ) )
                                {
                                    if ( this.fullname.matches( "([aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ][-]?[\\s]?){6,45}" ) )
                                    {
                                        if ( this.phone.matches( "[\\(\\+]?([0-9]{3,4})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})" ) )
                                        {
                                            if ( this.address.matches( "([aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ0-9][-,._\"']*[\\s]?){6,45}" ) )
                                            {
                                                localMessage = "Success";
                                            }
                                            else
                                            {
                                                localMessage = "Adress field must have at least 6 characters, maximum 45 characters, can contain some special characters.";
                                            }
                                        }
                                        else
                                        {
                                            localMessage = "Phone field is not valid.";
                                        }
                                    }
                                    else
                                    {
                                        localMessage = "Fullname field must contain only non-numeric characters, must be at least 6 characters and maximum 45 characters.";
                                    }
                                }
                                else
                                {
                                    localMessage = "Password field must start with one character, must contain no special characters, no spaces, have at least 6 characters and maximum 20 characters.";
                                }
                            }
                            else
                            {
                                localMessage = "Username field must start with one character, must contain no special characters, no spaces, have at least 6 characters and maximum 20 characters.";
                            }
                        }
                        else
                        {
                            localMessage = "Address field cannot be empty.";
                        }
                    }
                    else
                    {
                        localMessage = "Phone field cannot be empty.";
                    }
                }
                else
                {
                   localMessage = "Fullname field cannot be empty.";
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

    public CustomerController() {}
    public Customers getCustomer() { return customer; }
    public void setCustomer(Customers customer) { this.customer = customer; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getSearchCustomertByUsername() { return searchCustomertByUsername; }
    public void setSearchCustomertByUsername(String searchCustomertByUsername) { this.searchCustomertByUsername = searchCustomertByUsername; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }  
}
