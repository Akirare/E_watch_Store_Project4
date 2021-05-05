package Controller;

import DAO.BrandsFacadeLocal;
import Entity.Brands;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

@Named(value = "BrandController")
@SessionScoped
public class BrandController extends AbstractController implements Serializable , ValueChangeListener
{
    @EJB
    private BrandsFacadeLocal   brandsFacade;
    private int                 BrandId;
    private String              BrandName;
    private String              selectedBrand = "All";
    private List<Brands>        brandList;
    private String              validationMessage;
    private String              url;
    
    @Override
    public void processValueChange(ValueChangeEvent vce) throws AbortProcessingException 
    {
        this.selectedBrand = vce.getNewValue().toString();
    }
    
    public List<Brands> findAll()
    {
        if( "All".equals(this.selectedBrand) )
        {
            this.brandList = this.brandsFacade.findAll();
        }
        else
        {
            this.brandList = findBrandByBrandName();
        }
        
        return this.brandList;
    }
    
    public List<Brands> findBrandByBrandName()
    {
        List<Brands> resultList = new ArrayList<>();
        
        for( Brands currentBrand : this.brandsFacade.findAll() )
        {
            if( Objects.equals( currentBrand.getBrandName() , this.selectedBrand ) )
            {
                resultList.add( currentBrand );
            }
        }
        
        return resultList;
    }
    
    public String create()
    {
        this.validationMessage = checkBrandField();
        
        if ( "Success".equals( this.validationMessage ) )
        {
            this.validationMessage = checkBrandNameExisted();
            
            if ( "Success".equals( this.validationMessage ))
            {
                Brands handlingBrand = new Brands( this.BrandName );
            
                this.brandsFacade.create( handlingBrand );

                this.url = "admin_Brand_List.xhtml?faces-redirect=true";
            }
            else
            {
                this.url = "admin_Brand_Create.xhtml?faces-redirect=true";
            }
        }
        else
        {
            this.url = "admin_Brand_Create.xhtml?faces-redirect=true";
        }

        super.showAlert( this.validationMessage );
        
        return this.url;
    }
    
    public String update()
    {
        this.validationMessage = checkBrandField();
        
        if ( "Success".equals( this.validationMessage ) )
        {
            this.validationMessage = checkBrandNameExisted();
            
            if( "Success".equals( this.validationMessage ) )
            {
                Brands handlingBrand = new Brands( this.BrandId , this.BrandName );
            
                this.brandsFacade.edit(handlingBrand );

                this.url = "admin_Brand_List.xhtml?faces-redirect=true";
            }
            else
            {
                this.url = "admin_Brand_Update.xhtml?faces-redirect=true";
            }
        }
        else
        {
            this.url = "admin_Brand_Update.xhtml?faces-redirect=true";
        }
        
        super.showAlert( this.validationMessage );
        
        return this.url;
    }
    
    public String remove( Brands currentBrand )
    {
        this.brandsFacade.remove( currentBrand );
        
        this.url    =   "admin_Brand_List.xhtml?faces-redirect=true";
        
        super.showAlert( "Remove succesful" );
        
        return this.url;
    }
    
    public String redirect( Brands brand )
    {
        this.BrandId    = brand.getBrandId();
        this.BrandName  = brand.getBrandName();
        
        this.url        =   "admin_Brand_Update?faces-redirect=true";
        
        return this.url;
    }
    
    public String checkBrandNameExisted()
    {
        String localMessage = "Success";
        
        for ( Brands currentBrand : this.brandsFacade.findAll() )
        {
            if ( Objects.equals( currentBrand.getBrandName() , this.BrandName ) )
            {
                localMessage = "This brand name has already existed";
                
                break;
            }
        }
        
        return localMessage;
    }
    
    public String checkBrandField()
    {
        String localMessage;
        
        if ( !this.BrandName.isEmpty() && this.BrandName != null )
        {
            if( this.BrandName.matches( "[A-z0-9\\s]{1,100}" ) ) 
            {
                localMessage = "Success";
            }
            else
            {
                localMessage = "Brand field must contain no special characters, have at least one characters and maximum 100 characters.";
            }
        }
        else
        {
            localMessage = "Brand field cannot be empty.";
        }
        
        return localMessage;
    }
    
    public BrandController() {}
    public int getBrandId() { return BrandId; }
    public void setBrandId(int BrandId) { this.BrandId = BrandId; }
    public String getBrandName() { return BrandName; }
    public void setBrandName(String BrandName) { this.BrandName = BrandName; }
    public String getSelectedBrand() { return selectedBrand; }
    public void setSelectedBrand(String selectedBrand) { this.selectedBrand = selectedBrand; }  
    public List<Brands> getBrandList() { return brandList; }
    public void setBrandList(List<Brands> brandList) { this.brandList = brandList; } 
    public BrandsFacadeLocal getBrandsFacade() { return brandsFacade; }
}
