/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    @NamedQuery(name = "Section.findAll", query = "SELECT s FROM Section s"),
    @NamedQuery(name = "Section.findByPresentationId", query = "SELECT s FROM Section s WHERE s.sectionPK.presentationId = :presentationId"),
    @NamedQuery(name = "Section.findBySectionId", query = "SELECT s FROM Section s WHERE s.sectionPK.sectionId = :sectionId"),
    @NamedQuery(name = "Section.findByTitle", query = "SELECT s FROM Section s WHERE s.title = :title"),
    @NamedQuery(name = "Section.findByStart", query = "SELECT s FROM Section s WHERE s.starttime = :starttime"),
    @NamedQuery(name = "Section.findByEnd", query = "SELECT s FROM Section s WHERE s.endtime = :endtime")})
public class Section implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SectionPK sectionPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String title;
    @Lob
    @Size(max = 65535)
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtime;
    @JoinColumn(name = "presentation_id", referencedColumnName = "presentation_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Presentation presentation;

    public Section() {
    }

    public Section(SectionPK sectionPK) {
        this.sectionPK = sectionPK;
    }

    public Section(SectionPK sectionPK, String title) {
        this.sectionPK = sectionPK;
        this.title = title;
    }

    public Section(int presentationId, int sectionId) {
        this.sectionPK = new SectionPK(presentationId, sectionId);
    }

    public SectionPK getSectionPK() {
        return sectionPK;
    }

    public void setSectionPK(SectionPK sectionPK) {
        this.sectionPK = sectionPK;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
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
        hash += (sectionPK != null ? sectionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Section)) {
            return false;
        }
        Section other = (Section) object;
        if ((this.sectionPK == null && other.sectionPK != null) || (this.sectionPK != null && !this.sectionPK.equals(other.sectionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Section[ sectionPK=" + sectionPK + " ]";
    }
    
}
