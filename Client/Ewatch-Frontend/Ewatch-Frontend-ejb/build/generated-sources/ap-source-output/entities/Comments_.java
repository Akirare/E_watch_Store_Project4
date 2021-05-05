package entities;

import entities.Customers;
import entities.Products;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-04-13T22:54:41")
@StaticMetamodel(Comments.class)
public class Comments_ { 

    public static volatile SingularAttribute<Comments, Products> productId;
    public static volatile SingularAttribute<Comments, Date> commentDate;
    public static volatile SingularAttribute<Comments, Customers> customerId;
    public static volatile SingularAttribute<Comments, Integer> commentId;
    public static volatile SingularAttribute<Comments, String> comment;

}