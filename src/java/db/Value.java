/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author fran
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Value.findAll", query = "SELECT v FROM Value v"),
    @NamedQuery(name = "Value.findByValueId", query = "SELECT v FROM Value v WHERE v.valuePK.valueId = :valueId"),
    @NamedQuery(name = "Value.findByValue", query = "SELECT v FROM Value v WHERE v.value = :value"),
    @NamedQuery(name = "Value.findByPropertyId", query = "SELECT v FROM Value v WHERE v.valuePK.propertyId = :propertyId")})
public class Value implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ValuePK valuePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String value;
    @JoinColumn(name = "property_id", referencedColumnName = "property_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Property property;

    public Value() {
    }

    public Value(ValuePK valuePK) {
        this.valuePK = valuePK;
    }

    public Value(ValuePK valuePK, String value) {
        this.valuePK = valuePK;
        this.value = value;
    }

    public Value(int valueId, int propertyId) {
        this.valuePK = new ValuePK(valueId, propertyId);
    }

    public ValuePK getValuePK() {
        return valuePK;
    }

    public void setValuePK(ValuePK valuePK) {
        this.valuePK = valuePK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (valuePK != null ? valuePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Value)) {
            return false;
        }
        Value other = (Value) object;
        if ((this.valuePK == null && other.valuePK != null) || (this.valuePK != null && !this.valuePK.equals(other.valuePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Value[ valuePK=" + valuePK + " ]";
    }
    
}
