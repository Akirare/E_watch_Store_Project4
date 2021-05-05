package Entity;

import Entity.Customers;
import Entity.Orderdetails;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-04-14T21:40:05")
@StaticMetamodel(Orders.class)
public class Orders_ { 

    public static volatile SingularAttribute<Orders, Integer> orderId;
    public static volatile ListAttribute<Orders, Orderdetails> orderdetailsList;
    public static volatile SingularAttribute<Orders, Customers> customerId;
    public static volatile SingularAttribute<Orders, String> orderStatus;
    public static volatile SingularAttribute<Orders, String> description;
    public static volatile SingularAttribute<Orders, Date> deliveryDate;
    public static volatile SingularAttribute<Orders, Date> orderDate;

}