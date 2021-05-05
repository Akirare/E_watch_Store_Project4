/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.CommentsFacadeLocal;
import beans.CustomersFacadeLocal;
import beans.ProductsFacadeLocal;
import entities.Comments;
import entities.Customers;
import entities.Products;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author yup
 */
@Named(value = "commentManagedBean")
@SessionScoped
public class CommentManagedBean implements Serializable {

    @EJB
    private CustomersFacadeLocal customersFacade;

    public CustomersFacadeLocal getCustomersFacade() {
        return customersFacade;
    }

    public void setCustomersFacade(CustomersFacadeLocal customersFacade) {
        this.customersFacade = customersFacade;
    }
    private Customers customer;

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    @EJB
    private ProductsFacadeLocal productsFacade;

    public ProductsFacadeLocal getProductsFacade() {
        return productsFacade;
    }

    public void setProductsFacade(ProductsFacadeLocal productsFacade) {
        this.productsFacade = productsFacade;
    }

    private Products product;

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    @EJB
    private CommentsFacadeLocal commentsFacade;

    private Comments comment = new Comments();

    public Comments getComment() {
        return comment;
    }

    public void setComment(Comments comment) {
        this.comment = comment;
    }

    public CommentManagedBean() {
        product = new Products();
        customer = new Customers();
    }

    public List<Comments> findAll() {
        return this.commentsFacade.findAll();
    }
// khong su dung phuong thuc nay, co the bo, xoa luon trong commentsFacade va commentsFacadeLocal
    public List<Comments> findByProductId(int query){
        return this.commentsFacade.findByProductId(1);
    }
// khong su dung duoc phuong thuc nay, co the bo, xoa luon trong commentsFacade va commentsFacadeLocal    
    public int countByProductId(int id){
        return this.commentsFacade.countByProductId(id);
    }
    
    public String create(Products p) {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        String mdate = dateFormat.format(date.getTime());

        customer = this.customersFacade.find(session.getAttribute("customerId"));
        
        this.comment.setCustomerId(this.customersFacade.find(customer.getCustomerId()));
        this.comment.setCommentDate(date);
        this.comment.setProductId(this.productsFacade.find(p.getProductId()));

        System.out.println("==================================");
        System.out.println("CustomerId: " + this.customer.getCustomerId());
        System.out.println("ProductId: " + comment.getProductId());
        System.out.println("Comment: " + this.comment.getComment());
        System.out.println("dateFormat: " + dateFormat.format(date.getTime()));
        System.out.println("date: " + date);

        this.commentsFacade.create(comment);
        this.comment = new Comments();
        RequestContext.getCurrentInstance().execute("alert('You comment has been sent!');");
        return "product-detail";
    }
    
}
