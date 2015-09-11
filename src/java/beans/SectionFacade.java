/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import db.Section;

/**
 *
 * @author fran
 */
@Stateless
public class SectionFacade extends AbstractFacade<Section> {
    @PersistenceContext(unitName = "fidbacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SectionFacade() {
        super(Section.class);
    }
    
}
