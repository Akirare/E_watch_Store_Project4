/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.OrderdetailsFacadeLocal;
import beans.OrdersFacadeLocal;
import beans.ProductsFacadeLocal;
import entities.Orderdetails;
import entities.Orders;
import entities.Products;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author yup
 */
@Named(value = "orderdetailController")
@SessionScoped
public class OrderdetailController implements Serializable {

    @EJB
    private ProductsFacadeLocal productsFacade;

    @EJB
    private OrdersFacadeLocal ordersFacade;
    private Orders order;

    @EJB
    private OrderdetailsFacadeLocal orderdetailsFacade;
    private Orderdetails orderdetail = new Orderdetails();

    private Products product = new Products();

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public Orderdetails getOrderdetail() {
        return orderdetail;
    }

    public void setOrderdetail(Orderdetails orderdetail) {
        this.orderdetail = orderdetail;
    }

    public OrderdetailController() {
//        order = new Orders();
    }

    public List<Orderdetails> findAll() {
        return this.orderdetailsFacade.findAll();
    }

    public void test() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        System.out.println("=================================");
        System.out.println("orderdetailOnSession: " + session.getAttribute("orderSession"));
    }

    public List<Orderdetails> findByOrderId() {
        List<Orderdetails> ods = null;
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
            System.out.println("11111111111111111111111111111111111111111111111111");
//            Orders od = this.ordersFacade.find(session.getAttribute("orderSession"));
            Orders od = (Orders) session.getAttribute("orderSession");
            System.out.println("=================================");
            System.out.println("orderSession: " + od);
            ods = this.orderdetailsFacade.findByOrderId(od);

        } catch (Exception e) {
            System.out.println("exception: " +e.getMessage());
        }
        return ods;
    }

    public void delete(Orderdetails orderdetail) {
        this.orderdetailsFacade.remove(orderdetail);
    }
    
}
