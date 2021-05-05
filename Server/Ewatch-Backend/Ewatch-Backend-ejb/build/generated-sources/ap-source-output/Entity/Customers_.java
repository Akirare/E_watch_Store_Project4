package Entity;

import Entity.Comments;
import Entity.Orders;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-04-14T21:40:05")
@StaticMetamodel(Customers.class)
public class Customers_ { 

    public static volatile SingularAttribute<Customers, String> customerAddress;
    public static volatile SingularAttribute<Customers, String> customerStatus;
    public static volatile ListAttribute<Customers, Comments> commentsList;
    public static volatile SingularAttribute<Customers, String> customerPassword;
    public static volatile SingularAttribute<Customers, String> customerId;
    public static volatile SingularAttribute<Customers, String> customerName;
    public static volatile SingularAttribute<Customers, String> phoneNo;
    public static volatile ListAttribute<Customers, Orders> ordersList;

}