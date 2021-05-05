/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.Comments;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author yup
 */
@Stateless
public class CommentsFacade extends AbstractFacade<Comments> implements CommentsFacadeLocal {

    @PersistenceContext(unitName = "Ewatch-Frontend-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommentsFacade() {
        super(Comments.class);
    }
    
    @Override
    public List<Comments> findByProductId(int query) {
        List<Comments> comments = em.createQuery("SELECT c FROM Comments c WHERE c.productId = :productId")
                .setParameter("productId", query).getResultList();
        return comments;
    }

    @Override
    public int countByProductId(int id) {
        List<Comments> comments = em.createQuery("SELECT c FROM Comments c WHERE c.productId = :productId")
                .setParameter("productId", id).getResultList();
        return comments.size();
    }
}
