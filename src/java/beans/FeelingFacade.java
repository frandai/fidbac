/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import db.Feeling;
import java.util.List;

/**
 *
 * @author fran
 */
@Stateless
public class FeelingFacade extends AbstractFacade<Feeling> {
    @PersistenceContext(unitName = "fidbacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FeelingFacade() {
        super(Feeling.class);
    }

    public List<Feeling> findDefaultFeelings() {
        List<Feeling> feelings = this.findRange(new int[] {0,4});
        return feelings;
    }
    
}
