/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import db.Property;
import db.Property_;
import db.UserHaveProperty;
import java.util.Properties;

/**
 *
 * @author fran
 */
@Stateless
public class PropertyFacade extends AbstractFacade<Property> {
    @PersistenceContext(unitName = "fidbacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PropertyFacade() {
        super(Property.class);
    }
    
    public List<Property> getDefaultPropertires() {
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
            Root<Property> propertyRoot = cq.from(Property.class);
            cq.select(propertyRoot);
            cq.where(cb.equal(propertyRoot.get(Property_.isdefault), 1));
            return getEntityManager().createQuery(cq).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public ArrayList<UserHaveProperty> getDefaultPropertires(int userId) {
        ArrayList<UserHaveProperty> propertires = new ArrayList<>();
        List<Property> defaultPropertires = getDefaultPropertires();
        
        if(defaultPropertires == null) { return null; }
        
        for (Property defaultProperty : defaultPropertires) {
            propertires.add(new UserHaveProperty(userId, defaultProperty.getPropertyId()));
        }
        
        return propertires;
    }
    
    public List<Property> getNotDefaultPropertiresWithDescription(String query) {
        return getNotDefaultPropertiresWithDescription(query, false);
    }
    
    public Property getNotDefaultPropertireWithDescription(String query) {
        return getNotDefaultPropertiresWithDescription(query, true).get(0);
    }

    public List<Property> getNotDefaultPropertiresWithDescription(String query, boolean exact) {
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
            Root<Property> propertyRoot = cq.from(Property.class);
            cq.select(propertyRoot);
            cq.where(cb.and(
                cb.equal(propertyRoot.get(Property_.isdefault), 0),
                cb.like(cb.lower(propertyRoot.get(Property_.description)), exact? query : "%"+query+"%")
            ));
            return getEntityManager().createQuery(cq).getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
