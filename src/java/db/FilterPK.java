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
public class FilterPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "presentation_id")
    private int presentationId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "filter_id")
    private int filterId;

    public FilterPK() {
    }

    public FilterPK(int presentationId, int filterId) {
        this.presentationId = presentationId;
        this.filterId = filterId;
    }

    public int getPresentationId() {
        return presentationId;
    }

    public void setPresentationId(int presentationId) {
        this.presentationId = presentationId;
    }

    public int getFilterId() {
        return filterId;
    }

    public void setFilterId(int filterId) {
        this.filterId = filterId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) presentationId;
        hash += (int) filterId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FilterPK)) {
            return false;
        }
        FilterPK other = (FilterPK) object;
        if (this.presentationId != other.presentationId) {
            return false;
        }
        if (this.filterId != other.filterId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.FilterPK[ presentationId=" + presentationId + ", filterId=" + filterId + " ]";
    }
    
}
