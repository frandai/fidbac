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
public class UserHavePropertyPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "property_id")
    private int propertyId;

    public UserHavePropertyPK() {
    }

    public UserHavePropertyPK(int userId, int propertyId) {
        this.userId = userId;
        this.propertyId = propertyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        hash += (int) userId;
        hash += (int) propertyId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserHavePropertyPK)) {
            return false;
        }
        UserHavePropertyPK other = (UserHavePropertyPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.propertyId != other.propertyId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.UserHavePropertyPK[ userId=" + userId + ", propertyId=" + propertyId + " ]";
    }
    
}
