/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import db.PresentationHaveFeeling;

/**
 *
 * @author fran
 */
@Stateless
public class PresentationHaveFeelingFacade extends AbstractFacade<PresentationHaveFeeling> {
    @PersistenceContext(unitName = "fidbacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PresentationHaveFeelingFacade() {
        super(PresentationHaveFeeling.class);
    }
    
}
