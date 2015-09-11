/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.List;
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
    @NamedQuery(name = "Filter.findAll", query = "SELECT f FROM Filter f"),
    @NamedQuery(name = "Filter.findByPresentationId", query = "SELECT f FROM Filter f WHERE f.filterPK.presentationId = :presentationId"),
    @NamedQuery(name = "Filter.findByOperation", query = "SELECT f FROM Filter f WHERE f.operation = :operation"),
    @NamedQuery(name = "Filter.findByValue", query = "SELECT f FROM Filter f WHERE f.value = :value"),
    @NamedQuery(name = "Filter.findByFilterId", query = "SELECT f FROM Filter f WHERE f.filterPK.filterId = :filterId")})
public class Filter implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FilterPK filterPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String operation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String value;
    @JoinColumn(name = "property_id", referencedColumnName = "property_id")
    @ManyToOne(optional = false)
    private Property propertyId;
    @JoinColumn(name = "presentation_id", referencedColumnName = "presentation_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Presentation presentation;

    public String getDescription() {
        String avalue = getValue();
        if (getPropertyId().getType() == 2) {
            avalue = getValueFromList(avalue);
        }
        return getPropertyId().getDescription() + " " + getOperation().toUpperCase() + " " + avalue;
    }

    public Filter() {
    }

    public Filter(FilterPK filterPK) {
        this.filterPK = filterPK;
    }

    public Filter(FilterPK filterPK, String operation, String value) {
        this.filterPK = filterPK;
        this.operation = operation;
        this.value = value;
    }

    public Filter(int presentationId, int filterId) {
        this.filterPK = new FilterPK(presentationId, filterId);
    }

    public FilterPK getFilterPK() {
        return filterPK;
    }

    public void setFilterPK(FilterPK filterPK) {
        this.filterPK = filterPK;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Property getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Property propertyId) {
        this.propertyId = propertyId;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    public boolean accepts(List<UserHaveProperty> propertires) {
        if (propertires == null) {
            return false;
        }
        for (UserHaveProperty uP : propertires) {
            if (propertyEquals(uP, getPropertyId())) {
                if (this.operation.equals("eq")) {
                    return (uP.getValue().toLowerCase().equals(this.value.toLowerCase()))
                            || (uP.getProperty().getType() == 2 && uP.getValue().toLowerCase().equals(getValueFromList(value).toLowerCase()));
                } else if (this.operation.equals("ne")) {
                    return !(uP.getValue().toLowerCase().equals(this.value.toLowerCase())
                            || (uP.getProperty().getType() == 2 && uP.getValue().toLowerCase().equals(getValueFromList(value).toLowerCase())));
                } else if (this.operation.equals("gt") || this.operation.equals("lt")) {
                    if(uP.getProperty().getType() == 3 && (uP.getValueAsDate() == null || uP.getValueAsDate(this.value) == null)) {
                        return false;
                    }
                    switch (uP.getProperty().getType()) {
                        case 1:
                            return this.operation.equals("gt")
                                    ? (uP.getValue().compareToIgnoreCase(this.value) < 0)
                                    : (uP.getValue().compareToIgnoreCase(this.value) > 0);
                        case 2:
                        case 4:
                            return this.operation.equals("gt")
                                    ? (Integer.parseInt(uP.getValue()) > Integer.parseInt(this.value))
                                    : (Integer.parseInt(uP.getValue()) < Integer.parseInt(this.value));
                        case 3:
                            return this.operation.equals("gt")
                                    ? (uP.getValueAsDate().compareTo(uP.getValueAsDate(this.value)) > 0)
                                    : (uP.getValueAsDate().compareTo(uP.getValueAsDate(this.value)) < 0);

                    }
                }
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (filterPK != null ? filterPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Filter)) {
            return false;
        }
        Filter other = (Filter) object;
        if ((this.filterPK == null && other.filterPK != null) || (this.filterPK != null && !this.filterPK.equals(other.filterPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.Filter[ filterPK=" + filterPK + " ]";
    }

    private boolean propertyEquals(UserHaveProperty uP, Property propertyId) {
        return uP.getProperty().getPropertyId().equals(propertyId.getPropertyId())
                || (uP.getProperty().getDescription().equals(propertyId.getDescription())
                && uP.getProperty().getType() == propertyId.getType());
    }

    private String getValueFromList(String avalue) {
        for (Value v : getPropertyId().getValueList()) {
            if (v.getValuePK().getValueId().toString().equals(avalue)) {
                return v.getValue();
            }
        }
        return "";
    }

}
