package Entity;

import Entity.Brands;
import Entity.Comments;
import Entity.Orderdetails;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-04-14T21:40:05")
@StaticMetamodel(Products.class)
public class Products_ { 

    public static volatile SingularAttribute<Products, Float> unitPrice;
    public static volatile ListAttribute<Products, Comments> commentsList;
    public static volatile SingularAttribute<Products, String> productImage;
    public static volatile SingularAttribute<Products, Integer> quantity;
    public static volatile SingularAttribute<Products, Integer> productId;
    public static volatile SingularAttribute<Products, Brands> brandId;
    public static volatile ListAttribute<Products, Orderdetails> orderdetailsList;
    public static volatile SingularAttribute<Products, String> description;
    public static volatile SingularAttribute<Products, String> productName;
    public static volatile SingularAttribute<Products, String> productType;

}