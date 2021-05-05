package Controller;

import DAO.CustomersFacadeLocal;
import DAO.OrdersFacadeLocal;
import DAO.ProductsFacadeLocal;
import Entity.Customers;
import Entity.Orders;
import Entity.Products;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.Date;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

@Named( value = "OrderController" )
@SessionScoped
public class OrderController extends AbstractController implements Serializable , ValueChangeListener 
{
    @EJB
    private ProductsFacadeLocal     productsFacade;
    @EJB
    private CustomersFacadeLocal    customersFacade;
    @EJB
    private OrdersFacadeLocal       ordersFacade;
    private Customers               currentCustomer;
    private Orders                  order;
    private String                  orderStatus;
    private Date                    expectedDeliveryDate;
    private String                  currentStatus = "All";
    private String                  url = "admin_Order_List.xhtml?faces-redirect=true";
 
    @Override
    public void processValueChange(ValueChangeEvent vce) throws AbortProcessingException 
    {
        this.currentStatus = vce.getNewValue().toString();
    }
    
    public List<Orders> findAll()
    { 
        List<Orders> resultList     = new ArrayList<>();
        List<Orders> allOrdersList  = this.ordersFacade.findAll();
        
        if( "All".equals( this.currentStatus ) )
        {
            resultList = allOrdersList;
        }
        else
        {
            for( Orders currentOrder : allOrdersList )
            {
                if( this.currentStatus.equals( currentOrder.getOrderStatus() ) )
                {
                    resultList.add( currentOrder );
                }
            }
        }
        
        return resultList;
    }
  
    public String approvalOrder( Orders currentOrder )
    {
        if( this.expectedDeliveryDate != null )
        {
            currentOrder.setOrderStatus( "Being Delivered" );
            currentOrder.setDeliveryDate( this.expectedDeliveryDate );

            this.ordersFacade.edit( currentOrder );

            super.showAlert( "Approval Order " + currentOrder.getOrderId() + " Success." );
        }
        else
        {
            super.showAlert( "Expected Delivery Date cannot be null." );
        }
        
        return this.url;
    }
    
    public String cancelOrder( Orders currentOrder )
    {
        currentOrder.setOrderStatus( "Cancel" );
        currentOrder.setDeliveryDate( null );
        
        this.ordersFacade.edit( currentOrder );
        
        super.showAlert("Cancel Order " + currentOrder.getOrderId() + "Success.");
        
        return this.url;
    }
    
    public String deliveried( Orders currentOrder )
    {
        currentOrder.setOrderStatus( "Delivered" );
        currentOrder.setDeliveryDate( this.expectedDeliveryDate );
        
        this.ordersFacade.edit( currentOrder );
        
        super.showAlert("Order " + currentOrder.getOrderId() + " Deliveried.");
        
        return this.url;
    }
    
        public String convertCustomerIdToCustomerName( Customers customer )
    {
        String temp = "";
        
        for( Customers current : this.customersFacade.findAll() )
        {
            if( Objects.equals( current.getCustomerId() , customer.getCustomerId()) )
            {
                temp = current.getCustomerId();
                break;
            }
        }
        
        return temp;
    }
    
    public String convertProductIdToProductName( Products product )
    {
        String temp = "";
        
        for( Products current : this.productsFacade.findAll() )
        {
            if( Objects.equals( current.getProductId() , product.getProductId() ) )
            {
                temp = current.getProductName();
                break;
            }
        }
        
        return temp;
    }
    
    public OrderController() {}
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public Customers getCurrentCustomer() { return currentCustomer; }
    public void setCurrentCustomer(Customers currentCustomer) { this.currentCustomer = currentCustomer; }
    public Orders getOrder() { return order; }
    public void setOrder(Orders order) { this.order = order; }
    public Date getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public void setExpectedDeliveryDate(Date expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }
    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }
}
