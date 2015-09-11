/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import db.Anoption;

/**
 *
 * @author fran
 */
@Stateless
public class AnoptionFacade extends AbstractFacade<Anoption> {
    @PersistenceContext(unitName = "fidbacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AnoptionFacade() {
        super(Anoption.class);
    }
    
}
