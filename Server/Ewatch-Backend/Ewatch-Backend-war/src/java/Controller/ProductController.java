package Controller;

import DAO.BrandsFacadeLocal;
import DAO.ProductsFacadeLocal;
import Entity.Brands;
import Entity.Products;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

@Named(value = "ProductController")
@SessionScoped
public class ProductController extends AbstractController implements Serializable
{
    @EJB
    private ProductsFacadeLocal productsFacade;
    @EJB
    private BrandsFacadeLocal   brandsFacade;
    private String              searchProductByOrderId;
    private List<Products>      productsList;
    private List<Brands>        brandsList;
    private int                 ProductId;
    private Brands              BrandId;
    private String              ProductName;
    private String              ProductImage;
    private String              ProductType;
    private String              UnitPriceString;
    private float               UnitPrice;
    private String              QuantityString;
    private int                 Quantity;
    private String              Description;
    private int                 brandIdInt;
    private String              validationMessage;
    private String              url;
    private Part                file;
    private final int           limit_max_size  = 10240000;
    private final String        limit_type_file = "gif|jpg|png|jpeg";
    
    public String convertBrandIdToBrandName( Brands brand )
    {
        String temp = "";
        
        for( Brands current : this.brandsFacade.findAll() )
        {
            if( Objects.equals( current.getBrandId(), brand.getBrandId()) )
            {
                temp = current.getBrandName();
                break;
            }
        }
        
        return temp;
    }  

    public List<Products> findProductByOrderId( int orderId )
    {
        List<Products> resultList = new ArrayList<>();
        
        resultList.add(this.productsFacade.find( orderId ) );
        
        return resultList;
    }
    
    public List<Products> findAll()
    {
        if ( "".equals( this.searchProductByOrderId ) )
        {
            this.productsList = this.productsFacade.findAll();
        }
        else
        {
            try 
            {
                int orderID = Integer.parseInt( this.searchProductByOrderId );
                this.productsList = findProductByOrderId( orderID );
            } 
            catch (NumberFormatException e) 
            {
                this.productsList = this.productsFacade.findAll();
            }
        }
       
        return this.productsList;
    }
    
    public Brands findBrandByBrandId( int brandId )
    {   
        Brands result =  new Brands();
        for ( Brands currentBrand : this.brandsFacade.findAll() )
        {
            if ( currentBrand.getBrandId() == brandId )
            {
                result = currentBrand;
                break;
            }
        }
        
        return result;
    }
    
    public void create()
    {
        this.validationMessage = checkProductField();
        
        if ( this.validationMessage.equals( "Success." ) )
        {   
            if( this.file != null )
            {
                this.ProductImage = processUpload( this.file );
                processUploadFront(file, ProductName);

                this.BrandId = findBrandByBrandId( this.brandIdInt );

                Products handleProduct = new Products( ProductName , ProductImage , ProductType , UnitPrice , Quantity , Description , BrandId);

                this.productsFacade.create( handleProduct );
                
                this.url = "admin_Product_List.xhtml?faces-redirect=true";
            }
            else
            {
                this.validationMessage = "You must upload product image";
                
                this.url = "admin_Product_Create.xhtml?faces-redirect=true";
            }
        }
        else
        {
            this.url = "admin_Product_Create.xhtml?faces-redirect=true";
        }
        
        super.showAlert( this.validationMessage );
        
        super.reloadPage( this.url );
    }
    
    public String redirect( Products handlingProduct )
    {
        this.ProductId          =   handlingProduct.getProductId();
        this.ProductName        =   handlingProduct.getProductName();
        this.ProductImage       =   handlingProduct.getProductImage();
        this.ProductType        =   handlingProduct.getProductType();
        this.UnitPrice          =   handlingProduct.getUnitPrice();
        this.UnitPriceString    =   String.valueOf( this.UnitPrice );
        this.Quantity           =   handlingProduct.getQuantity();
        this.QuantityString     =   String.valueOf( this.Quantity );
        this.Description        =   handlingProduct.getDescription();
        this.BrandId            =   handlingProduct.getBrandId();
        this.brandIdInt         =   this.BrandId.getBrandId();
        this.url                =   "admin_Product_Update.xhtml?faces-redirect=true";
        
        return this.url;
    }
    
    public void edit()
    {
        this.validationMessage = checkProductField();
        
        if ( this.validationMessage.equals( "Success." ) )
        {   
            if( this.file != null )
            {
                deleteImage( this.ProductImage );
                
                this.ProductImage = processUpload( this.file );
                
                processUploadFront(file, ProductName);

                this.BrandId = findBrandByBrandId( this.brandIdInt );

                Products handleProduct = new Products( ProductId , ProductName , ProductImage , ProductType , UnitPrice , Quantity , Description , BrandId);

                this.productsFacade.edit( handleProduct );
                
                this.url = "admin_Product_List.xhtml?faces-redirect=true";
            }
            else
            {
                this.BrandId = findBrandByBrandId( this.brandIdInt );

                Products handleProduct = new Products( ProductId , ProductName , ProductType , UnitPrice , Quantity , Description , BrandId);

                this.productsFacade.edit( handleProduct );
                
                this.url = "admin_Product_List.xhtml?faces-redirect=true";
            }
        }
        else
        {
            this.url = "admin_Product_Update.xhtml?faces-redirect=true";
        }
        
        super.showAlert( this.validationMessage );
        
        super.reloadPage( this.url );
    }
    
    public String suspend( Products handleProduct )
    {
        handleProduct.setQuantity( 0 );
        
        this.productsFacade.edit( handleProduct );
        
        return this.url = "admin_Product_List.xhtml?faces-redirect=true";
    }
    
    public String checkProductField()
    {
        String localMessage;

            if ( !this.ProductName.isEmpty() && this.ProductName != null )
            {
                if ( !this.ProductType.isEmpty() && this.ProductType != null )
                {
                   if ( !this.UnitPriceString.isEmpty() && this.UnitPriceString != null )
                   {
                       if ( !this.QuantityString.isEmpty() && this.QuantityString != null )
                       {
                            if ( !this.Description.isEmpty() && this.Description != null )
                            {
                                if ( this.ProductName.matches( "([A-z0-9\\s]{4,20})" ) )
                                {
                                    if ( this.ProductType.matches( "^[A-z]([A-z0-9\\s]{4,45})" ) )
                                    {
                                        try 
                                        {
                                            this.UnitPrice = Float.parseFloat( this.UnitPriceString );
                                            try
                                            {
                                                this.Quantity = Integer.parseInt( this.QuantityString ) ;
                                                localMessage = "Success.";
                                            }
                                            catch (NumberFormatException ex)
                                            {
                                                localMessage = "Product quantity field must be a number";
                                            }
                                        }
                                        catch (NumberFormatException ex)
                                        {
                                            localMessage = "Product price field must be a number";
                                        }
                                    }
                                    else
                                    {
                                        localMessage = "Product type field must start with a character, must contain no special characters, have at least 5 characters and maximum 45 characters.";
                                    }
                                }
                                else
                                {
                                    localMessage = "Product name field must contain no special characters, have at least 5 characters and maximum 20 characters.";
                                }
                            }
                            else
                            {
                                localMessage = "Description field cannot be empty";
                            }
                       }
                       else
                       {
                           localMessage = "Product quantity field cannot be empty.";
                       }
                   }
                   else
                   {
                       localMessage = "Product price field cannot be empty.";
                   }
                }
                else 
                { 
                    localMessage = "Product type field cannot be empty.";
                }
            }
            else
            {
                localMessage = "Product name field cannot be empty.";
            }
        
        
        return localMessage;
    }
    
    public ProductController() {}
    public String getSearchProductByOrderId() { return searchProductByOrderId; }
    public void setSearchProductByOrderId(String searchProductByOrderId) { this.searchProductByOrderId = searchProductByOrderId; }
    public List<Products> getProductsList() { return productsList; }
    public void setProductsList(List<Products> productsList) { this.productsList = productsList; } 
    public int getProductId() { return ProductId; }
    public void setProductId(int ProductId) { this.ProductId = ProductId; }
    public Brands getBrandId() { return BrandId; }
    public void setBrandId(Brands BrandId) { this.BrandId = BrandId; }
    public String getProductName() { return ProductName; }
    public void setProductName(String ProductName) { this.ProductName = ProductName; }
    public String getProductImage() { return ProductImage; }
    public void setProductImage(String ProductImage) { this.ProductImage = ProductImage; }
    public String getProductType() { return ProductType; }
    public void setProductType(String ProductType) { this.ProductType = ProductType; }
    public String getUnitPriceString() { return UnitPriceString; }
    public void setUnitPriceString(String UnitPriceString) { this.UnitPriceString = UnitPriceString; }
    public float getUnitPrice() { return UnitPrice; }
    public void setUnitPrice(float UnitPrice) { this.UnitPrice = UnitPrice; }
    public String getQuantityString() { return QuantityString; }
    public void setQuantityString(String QuantityString) { this.QuantityString = QuantityString; }
    public int getQuantity() {  return Quantity; }
    public void setQuantity(int Quantity) { this.Quantity = Quantity; }
    public String getDescription() { return Description; }
    public void setDescription(String Description) { this.Description = Description; }  
    public List<Brands> getBrandsList() { return brandsFacade.findAll(); }
    public void setBrandsList(List<Brands> brandsList) { this.brandsList = brandsList; }
    public int getBrandIdInt() { return brandIdInt; }
    public void setBrandIdInt(int brandIdInt) { this.brandIdInt = brandIdInt; }
    public Part getFile() { return file; }
    public void setFile(Part file) { this.file = file; }

    public String processUpload( Part fileUpload ) 
    {
        String fileSaveData = "noimages.jpg";
        try 
        {
            if ( fileUpload.getSize() > 0 ) 
            {
                String submittedFileName = getFilename( fileUpload );
                if ( checkFileType( submittedFileName ) ) 
                {
                    if ( fileUpload.getSize() > this.limit_max_size ) 
                    {
                        FacesContext.getCurrentInstance().addMessage( null , new FacesMessage( FacesMessage.SEVERITY_INFO , "File size too large!", "" ) );
                    } 
                    else 
                    {
                        String currentFileName = submittedFileName;
                        String extension = currentFileName.substring(currentFileName.lastIndexOf("."), currentFileName.length());
                        Long nameRadom = Calendar.getInstance().getTimeInMillis();
                        String newfilename = nameRadom + extension;
                        fileSaveData             = newfilename;
//                        String contextPath = servletContext.getRealPath();
                        String fileSavePath     = "E:\\MyProgram\\Aptech\\Subject\\47_ejbProject\\srcOnGitHub\\Ewatch-Backend\\Ewatch-Backend-war\\web\\resources\\productImg";
                        
//                        String fileSavePath     = "172.16.160.199\\Project HK4\\CP1996M05\\Group 2";

                        try 
                        {
                            byte[]      fileContent = new byte[(int) fileUpload.getSize()];
                            InputStream in          = fileUpload.getInputStream();
                            in.read(fileContent);

                            File fileToCreate   = new File( fileSavePath , newfilename );
                            File folder         = new File( fileSavePath );
                            if ( !folder.exists() ) 
                            {
                                folder.mkdirs();
                            }
                            FileOutputStream fileOutStream = new FileOutputStream( fileToCreate );
                            System.out.println("2222222222222222222222222222222222222");
                            fileOutStream.write( fileContent );
                            fileOutStream.flush();
                            fileOutStream.close();
                            fileSaveData = newfilename;
                        } 
                        catch (IOException e) 
                        {
                            fileSaveData = "noimages.jpg";
                        }
                    }
                } 
                else 
                {
                    fileSaveData = "noimages.jpg";
                }
            }
        } 
        catch (Exception ex) 
        {
            fileSaveData = "noimages.jpg";
        }
//        fileSaveData = "resources/productImg/" + fileSaveData;
        return fileSaveData;
    }
    
    public String processUploadFront( Part fileUpload, String imgName ) 
    {
        String fileSaveData = "noimages.jpg";
        try 
        {
            if ( fileUpload.getSize() > 0 ) 
            {
                String submittedFileName = getFilename( fileUpload );
                if ( checkFileType( submittedFileName ) ) 
                {
                    if ( fileUpload.getSize() > this.limit_max_size ) 
                    {
                        FacesContext.getCurrentInstance().addMessage( null , new FacesMessage( FacesMessage.SEVERITY_INFO , "File size too large!", "" ) );
                    } 
                    else 
                    {
                        String currentFileName = submittedFileName;
                        String extension = currentFileName.substring(currentFileName.lastIndexOf("."), currentFileName.length());
                        Long nameRadom = Calendar.getInstance().getTimeInMillis();
                        String newfilename = imgName;
                        fileSaveData             = newfilename;
//                        String contextPath = servletContext.getRealPath();
                        String fileSavePath     = "E:\\MyProgram\\Aptech\\Subject\\47_ejbProject\\srcOnGitHub\\Ewatch-Frontend\\Ewatch-Frontend-war\\web\\vendors\\img\\product";
                        
//                        String fileSavePath     = "172.16.160.199\\Project HK4\\CP1996M05\\Group 2";

                        try 
                        {
                            byte[]      fileContent = new byte[(int) fileUpload.getSize()];
                            InputStream in          = fileUpload.getInputStream();
                            in.read(fileContent);

                            File fileToCreate   = new File( fileSavePath , newfilename );
                            File folder         = new File( fileSavePath );
                            if ( !folder.exists() ) 
                            {
                                folder.mkdirs();
                            }
                            FileOutputStream fileOutStream = new FileOutputStream( fileToCreate );
                            System.out.println("2222222222222222222222222222222222222");
                            fileOutStream.write( fileContent );
                            fileOutStream.flush();
                            fileOutStream.close();
                            fileSaveData = newfilename;
                        } 
                        catch (IOException e) 
                        {
                            fileSaveData = "noimages.jpg";
                        }
                    }
                } 
                else 
                {
                    fileSaveData = "noimages.jpg";
                }
            }
        } 
        catch (Exception ex) 
        {
            fileSaveData = "noimages.jpg";
        }
//        fileSaveData = "resources/productImg/" + fileSaveData;
        return fileSaveData;
    }

    private String getFilename( Part part ) 
    {
        for ( String cd : part.getHeader( "content-disposition" ).split( ";" )) 
        {
            if ( cd.trim().startsWith( "filename" ) ) 
            {
                String filename = cd.substring( cd.indexOf( '=' ) + 1 ).trim().replace( "\"" , "" );
                return filename.substring( filename.lastIndexOf( '/' ) + 1 ).substring( filename.lastIndexOf( '\\' ) + 1 );
            }
        }
        return null;
    }

    private boolean checkFileType( String fileName ) 
    {
        if ( fileName.length() > 0 ) 
        {
            String[] parts = fileName.split( "\\." );
            if ( parts.length > 0 ) 
            {
                String extention = parts[parts.length - 1];
                return this.limit_type_file.contains( extention );
            }
        }
        return false;
    }

    public void deleteImage( String imageName )
    {
        Path imagesPath = Paths.get("D:\\Ewatch-Backend\\Ewatch-Backend\\Ewatch-Backend-war\\web\\" 
                + imageName);
        try 
        {
            Files.delete(imagesPath);
            System.out.println("File " + imagesPath.toAbsolutePath().toString() + " successfully removed");
        } 
        catch (IOException e) 
        {
            System.err.println("Unable to delete " + imagesPath.toAbsolutePath().toString() + " due to...");
        }
    }
}
