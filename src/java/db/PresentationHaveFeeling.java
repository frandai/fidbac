/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author fran
 */
@Entity
@Table(name = "presentation_have_feeling")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PresentationHaveFeeling.findAll", query = "SELECT p FROM PresentationHaveFeeling p"),
    @NamedQuery(name = "PresentationHaveFeeling.findByPresentationId", query = "SELECT p FROM PresentationHaveFeeling p WHERE p.presentationHaveFeelingPK.presentationId = :presentationId"),
    @NamedQuery(name = "PresentationHaveFeeling.findByFeelingId", query = "SELECT p FROM PresentationHaveFeeling p WHERE p.presentationHaveFeelingPK.feelingId = :feelingId"),
    @NamedQuery(name = "PresentationHaveFeeling.findByAnorder", query = "SELECT p FROM PresentationHaveFeeling p WHERE p.order = :order"),
    @NamedQuery(name = "PresentationHaveFeeling.findByIsDefault", query = "SELECT p FROM PresentationHaveFeeling p WHERE p.isdefault = :isdefault")})
public class PresentationHaveFeeling implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PresentationHaveFeelingPK presentationHaveFeelingPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anorder")
    private int order;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isdefault")
    private short isdefault;
    @JoinColumn(name = "feeling_id", referencedColumnName = "feeling_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Feeling feeling;
    @JoinColumn(name = "presentation_id", referencedColumnName = "presentation_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Presentation presentation;

    public PresentationHaveFeeling() {
    }

    public PresentationHaveFeeling(PresentationHaveFeelingPK presentationHaveFeelingPK) {
        this.presentationHaveFeelingPK = presentationHaveFeelingPK;
    }

    public PresentationHaveFeeling(PresentationHaveFeelingPK presentationHaveFeelingPK, int order, short default1) {
        this.presentationHaveFeelingPK = presentationHaveFeelingPK;
        this.order = order;
        this.isdefault = default1;
    }

    public PresentationHaveFeeling(int presentationId, int feelingId) {
        this.presentationHaveFeelingPK = new PresentationHaveFeelingPK(presentationId, feelingId);
    }

    public PresentationHaveFeelingPK getPresentationHaveFeelingPK() {
        return presentationHaveFeelingPK;
    }

    public void setPresentationHaveFeelingPK(PresentationHaveFeelingPK presentationHaveFeelingPK) {
        this.presentationHaveFeelingPK = presentationHaveFeelingPK;
    }

    public int getOrder() {
        return order;
    }
    
    public char getOrderLetter() {
        return (char) ('a'+(order-1));
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public short getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(short isdefault) {
        this.isdefault = isdefault;
    }

    public Feeling getFeeling() {
        return feeling;
    }

    public void setFeeling(Feeling feeling) {
        this.feeling = feeling;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (presentationHaveFeelingPK != null ? presentationHaveFeelingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PresentationHaveFeeling)) {
            return false;
        }
        PresentationHaveFeeling other = (PresentationHaveFeeling) object;
        if ((this.presentationHaveFeelingPK == null && other.presentationHaveFeelingPK != null) || (this.presentationHaveFeelingPK != null && !this.presentationHaveFeelingPK.equals(other.presentationHaveFeelingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.PresentationHaveFeeling[ presentationHaveFeelingPK=" + presentationHaveFeelingPK + " ]";
    }
    
}
