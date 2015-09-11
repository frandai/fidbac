/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Embeddable
public class ValuePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "value_id")
    private Integer valueId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "property_id")
    private int propertyId;

    public ValuePK() {
    }

    public ValuePK(Integer valueId, int propertyId) {
        this.valueId = valueId;
        this.propertyId = propertyId;
    }

    public Integer getValueId() {
        return valueId;
    }

    public void setValueId(Integer valueId) {
        this.valueId = valueId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) valueId;
        hash += (int) propertyId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValuePK)) {
            return false;
        }
        ValuePK other = (ValuePK) object;
        if (this.valueId != other.valueId) {
            return false;
        }
        if (this.propertyId != other.propertyId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.ValuePK[ valueId=" + valueId + ", propertyId=" + propertyId + " ]";
    }
    
}
