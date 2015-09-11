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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
    @NamedQuery(name = "Feeling.findAll", query = "SELECT f FROM Feeling f"),
    @NamedQuery(name = "Feeling.findByFeelingId", query = "SELECT f FROM Feeling f WHERE f.feelingId = :feelingId"),
    @NamedQuery(name = "Feeling.findByName", query = "SELECT f FROM Feeling f WHERE f.name = :name"),
    @NamedQuery(name = "Feeling.findByColor", query = "SELECT f FROM Feeling f WHERE f.color = :color"),
    @NamedQuery(name = "Feeling.findByIcon", query = "SELECT f FROM Feeling f WHERE f.icon = :icon")})
public class Feeling implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "feeling_id")
    private Integer feelingId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String color;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String icon;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feeling")
    private List<PresentationHaveFeeling> presentationHaveFeelingList;

    public Feeling() {
    }

    public Feeling(Integer feelingId) {
        this.feelingId = feelingId;
    }

    public Feeling(Integer feelingId, String name, String color, String icon) {
        this.feelingId = feelingId;
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    public Integer getFeelingId() {
        return feelingId;
    }

    public void setFeelingId(Integer feelingId) {
        this.feelingId = feelingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color.startsWith("#")? color : "#"+color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @XmlTransient
    public List<PresentationHaveFeeling> getPresentationHaveFeelingList() {
        return presentationHaveFeelingList;
    }

    public void setPresentationHaveFeelingList(List<PresentationHaveFeeling> presentationHaveFeelingList) {
        this.presentationHaveFeelingList = presentationHaveFeelingList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (feelingId != null ? feelingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Feeling)) {
            return false;
        }
        Feeling other = (Feeling) object;
        if ((this.feelingId == null && other.feelingId != null) || (this.feelingId != null && !this.feelingId.equals(other.feelingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Feeling[ feelingId=" + feelingId + " ]";
    }
    
}
