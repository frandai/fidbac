/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import db.Presentation;
import db.Presentation_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

/**
 *
 * @author fran
 */
@Stateless
public class PresentationFacade extends AbstractFacade<Presentation> {
    @PersistenceContext(unitName = "fidbacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PresentationFacade() {
        super(Presentation.class);
    }
    
    public Presentation find(String code) {
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
            Root<Presentation> presentationRoot = cq.from(Presentation.class);
            cq.select(presentationRoot);
            cq.where(
                cb.equal(presentationRoot.get(Presentation_.code), code)
            );
            return (Presentation) getEntityManager().createQuery(cq).getSingleResult(); 
        } catch (Exception e) {
            return null;
        }
    }
}
