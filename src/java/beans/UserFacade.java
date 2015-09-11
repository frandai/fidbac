/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import db.User;
import db.User_;

/**
 *
 * @author fran
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {
    @PersistenceContext(unitName = "fidbacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    public User find(String username, String password) {
        try {
            username = username.toLowerCase();
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
            Root<User> userRoot = cq.from(User.class);
            cq.select(userRoot);
            cq.where(cb.and(
                    cb.equal(userRoot.get(User_.username), username),
                    cb.equal(userRoot.get(User_.password), password)
            )
            );
            return (User) getEntityManager().createQuery(cq).getSingleResult();     
        } catch (Exception e) {
            return null;
        }
    }

    public boolean exists(String username) {
        try {
            username = username.toLowerCase();
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
            Root<User> userRoot = cq.from(User.class);
            cq.select(userRoot);
            cq.where(
                cb.equal(userRoot.get(User_.username), username)
            );
            getEntityManager().createQuery(cq).getSingleResult(); 
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
