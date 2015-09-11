/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author fran
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Property.findAll", query = "SELECT p FROM Property p"),
    @NamedQuery(name = "Property.findByPropertyId", query = "SELECT p FROM Property p WHERE p.propertyId = :propertyId"),
    @NamedQuery(name = "Property.findByDescription", query = "SELECT p FROM Property p WHERE p.description = :description"),
    @NamedQuery(name = "Property.findByType", query = "SELECT p FROM Property p WHERE p.type = :type"),
    @NamedQuery(name = "Property.findByIsdefault", query = "SELECT p FROM Property p WHERE p.isdefault = :isdefault")})
public class Property implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "property_id")
    private Integer propertyId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String description;
    @Basic(optional = false)
    @NotNull
    private int type;
    @Basic(optional = false)
    @NotNull
    private short isdefault;
    @JoinTable(name = "property_mandatory", joinColumns = {
        @JoinColumn(name = "property_id", referencedColumnName = "property_id")}, inverseJoinColumns = {
        @JoinColumn(name = "presentation_id", referencedColumnName = "presentation_id")})
    @ManyToMany
    private List<Presentation> presentationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "property")
    private List<Value> valueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "property")
    private List<UserHaveProperty> userHavePropertyList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "propertyId")
    private List<Filter> filterList;
    @Transient
    private boolean propertySelected = false;

    public Property() {
    }

    public Property(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public Property(Integer propertyId, String description, int type, short isdefault) {
        this.propertyId = propertyId;
        this.description = description;
        this.type = type;
        this.isdefault = isdefault;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public short getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(short isdefault) {
        this.isdefault = isdefault;
    }

    @XmlTransient
    public List<Presentation> getPresentationList() {
        return presentationList;
    }

    public void setPresentationList(List<Presentation> presentationList) {
        this.presentationList = presentationList;
    }

    @XmlTransient
    public List<Value> getValueList() {
        return valueList;
    }

    public void setValueList(List<Value> valueList) {
        this.valueList = valueList;
    }

    @XmlTransient
    public List<UserHaveProperty> getUserHavePropertyList() {
        return userHavePropertyList;
    }

    public void setUserHavePropertyList(List<UserHaveProperty> userHavePropertyList) {
        this.userHavePropertyList = userHavePropertyList;
    }

    @XmlTransient
    public List<Filter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<Filter> filterList) {
        this.filterList = filterList;
    }

    public String getTypeDefinition() {
        switch(type) {
            case 1:
                return "text";
            case 2:
                return "list";
            case 3:
                return "date";
            case 4:
                return "number";
        }
        return "";
    }

    public boolean isPropertySelected() {
        return propertySelected;
    }

    public void setPropertySelected(boolean propertySelected) {
        this.propertySelected = propertySelected;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (propertyId != null ? propertyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Property)) {
            return false;
        }
        Property other = (Property) object;
        if ((this.propertyId == null && other.propertyId != null) || (this.propertyId != null && !this.propertyId.equals(other.propertyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Property[ propertyId=" + propertyId + " ]";
    }
    
}
