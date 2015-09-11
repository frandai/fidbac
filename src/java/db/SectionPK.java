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
public class SectionPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "presentation_id")
    private int presentationId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "section_id")
    private int sectionId;

    public SectionPK() {
    }

    public SectionPK(int presentationId, int sectionId) {
        this.presentationId = presentationId;
        this.sectionId = sectionId;
    }

    public int getPresentationId() {
        return presentationId;
    }

    public void setPresentationId(int presentationId) {
        this.presentationId = presentationId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) presentationId;
        hash += (int) sectionId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SectionPK)) {
            return false;
        }
        SectionPK other = (SectionPK) object;
        if (this.presentationId != other.presentationId) {
            return false;
        }
        if (this.sectionId != other.sectionId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.SectionPK[ presentationId=" + presentationId + ", sectionId=" + sectionId + " ]";
    }
    
}
