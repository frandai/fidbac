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
public class PresentationHaveFeelingPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "presentation_id")
    private int presentationId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feeling_id")
    private int feelingId;

    public PresentationHaveFeelingPK() {
    }

    public PresentationHaveFeelingPK(int presentationId, int feelingId) {
        this.presentationId = presentationId;
        this.feelingId = feelingId;
    }

    public int getPresentationId() {
        return presentationId;
    }

    public void setPresentationId(int presentationId) {
        this.presentationId = presentationId;
    }

    public int getFeelingId() {
        return feelingId;
    }

    public void setFeelingId(int feelingId) {
        this.feelingId = feelingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) presentationId;
        hash += (int) feelingId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PresentationHaveFeelingPK)) {
            return false;
        }
        PresentationHaveFeelingPK other = (PresentationHaveFeelingPK) object;
        if (this.presentationId != other.presentationId) {
            return false;
        }
        if (this.feelingId != other.feelingId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.PresentationHaveFeelingPK[ presentationId=" + presentationId + ", feelingId=" + feelingId + " ]";
    }
    
}
