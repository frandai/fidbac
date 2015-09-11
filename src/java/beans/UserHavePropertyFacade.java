/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import db.UserHaveProperty;

/**
 *
 * @author fran
 */
@Stateless
public class UserHavePropertyFacade extends AbstractFacade<UserHaveProperty> {
    @PersistenceContext(unitName = "fidbacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserHavePropertyFacade() {
        super(UserHaveProperty.class);
    }
    
}
