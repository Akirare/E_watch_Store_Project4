package Controller;

import DAO.CommentsFacadeLocal;
import Entity.Comments;
import Entity.Customers;
import Entity.Products;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named( value = "CommentController" )
@SessionScoped
public class CommentController extends AbstractController implements Serializable 
{
    @EJB
    private CommentsFacadeLocal     commentsFacade;
    private Products                product;
    private Customers               customer;
    private Comments                comment;
    private Date                    commentDate;
    private String                  searchCommentByUsername  = "";
    private List<Comments>          commentList;
    private String                  url;
    
    public List<Comments> findAll()
    {
        if( "".equals(this.searchCommentByUsername) )
        {
            commentList = this.commentsFacade.findAll();
        }
        else 
        {
            commentList = findAllCommentsByCustomerUsername();
        }
        
        return commentList;
    }
    
    public List<Comments> findAllCommentsByCustomerUsername()
    {      
        List<Comments> resultList = new ArrayList<>();
        
        for( Comments currentComment : this.commentsFacade.findAll() )
        {
            if( currentComment.getCustomerId().getCustomerId().contains(this.searchCommentByUsername ) )
            {
                resultList.add( currentComment );
            }
        }
        
        return resultList;
    }
    
    public String remove( Comments comment )
    {
        this.commentsFacade.remove( comment );
        
        this.url = "admin_Comment_List.xhtml?faces-redirect=true";
        
        super.showAlert( "Remove succesful" );
        
        return this.url;
    }

    public CommentController() {}
    public Products getProduct() { return product; }
    public void setProduct(Products product) { this.product = product; }
    public Customers getCustomer() { return customer; }
    public void setCustomer(Customers customer) { this.customer = customer; }
    public Comments getComment() { return comment; }
    public void setComment(Comments comment) { this.comment = comment; }
    public String getSearchCommentByUsername() { return searchCommentByUsername; }
    public void setSearchCommentByUsername(String searchCommentByUsername) { this.searchCommentByUsername = searchCommentByUsername; }
    public List<Comments> getCommentList() { return commentList; }
    public void setCommentList(List<Comments> commentList) { this.commentList = commentList; }
    public Date getCommentDate() {  return commentDate; }
    public void setCommentDate(Date commentDate) { this.commentDate = commentDate; }
}
