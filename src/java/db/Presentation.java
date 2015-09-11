/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    @NamedQuery(name = "Presentation.findAll", query = "SELECT p FROM Presentation p"),
    @NamedQuery(name = "Presentation.findByPresentationId", query = "SELECT p FROM Presentation p WHERE p.presentationId = :presentationId"),
    @NamedQuery(name = "Presentation.findByCode", query = "SELECT p FROM Presentation p WHERE p.code = :code"),
    @NamedQuery(name = "Presentation.findByTitle", query = "SELECT p FROM Presentation p WHERE p.title = :title"),
    @NamedQuery(name = "Presentation.findByStart", query = "SELECT p FROM Presentation p WHERE p.startdate = :startdate"),
    @NamedQuery(name = "Presentation.findByEnd", query = "SELECT p FROM Presentation p WHERE p.enddate = :enddate")})
public class Presentation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "presentation_id")
    private Integer presentationId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String title;
    @Lob
    @Size(max = 65535)
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startdate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date enddate;
    @JoinTable(name = "property_mandatory", joinColumns = {
        @JoinColumn(name = "presentation_id", referencedColumnName = "presentation_id")}, inverseJoinColumns = {
        @JoinColumn(name = "property_id", referencedColumnName = "property_id")})
    @ManyToMany
    private List<Property> propertyList;
    @JoinTable(name = "user_participates_presentation", joinColumns = {
        @JoinColumn(name = "presentation_id", referencedColumnName = "presentation_id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "user_id")})
    @ManyToMany
    private List<User> userList;
    @JoinTable(name = "user_admin_presentation", joinColumns = {
        @JoinColumn(name = "presentation_id", referencedColumnName = "presentation_id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "user_id")})
    @ManyToMany
    private List<User> userListAdmin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "presentation")
    private List<PresentationHaveFeeling> presentationHaveFeelingList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "presentation")
    private List<Question> questionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "presentation")
    private List<Filter> filterList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "presentation")
    private List<Section> sectionList;
    @Transient
    private String status = null;

    public Presentation() {
    }

    public Presentation(Integer presentationId) {
        this.presentationId = presentationId;
    }

    public Presentation(Integer presentationId, String code, String title) {
        this.presentationId = presentationId;
        this.code = code;
        this.title = title;
    }

    public Integer getPresentationId() {
        return presentationId;
    }

    public void setPresentationId(Integer presentationId) {
        this.presentationId = presentationId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    @XmlTransient
    public List<Property> getPropertyList() {
        return propertyList;
    }
    
    @XmlTransient
    public List<Property> getNotDefaultPropertyList() {
        ArrayList<Property> notDefaultProperty = new ArrayList<>();
        for (Property property : propertyList) {
            if (property.getIsdefault() == 0) {
                property.setPropertySelected(true);
                notDefaultProperty.add(property);
            }
        }
        return notDefaultProperty;
    }
    
    @XmlTransient
    public List<Property> getSelectedNotDefaultPropertyList() {
        ArrayList<Property> notDefaultProperty = new ArrayList<>();
        for (Property property : propertyList) {
            if (property.getIsdefault() == 0 && property.isPropertySelected()) {
                notDefaultProperty.add(property);
            }
        }
        return notDefaultProperty;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    @XmlTransient
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @XmlTransient
    public List<User> getUserListAdmin() {
        return userListAdmin;
    }

    public void setUserListAdmin(List<User> userListAdmin) {
        this.userListAdmin = userListAdmin;
    }

    @XmlTransient
    public List<PresentationHaveFeeling> getPresentationHaveFeelingList() {
        Collections.sort(presentationHaveFeelingList, new Comparator<PresentationHaveFeeling>() {
            @Override
            public int compare(PresentationHaveFeeling o1, PresentationHaveFeeling o2) {
                return new Integer(o1.getOrder()).compareTo(o2.getOrder());
            }
        });
        return presentationHaveFeelingList;
    }

    public void setPresentationHaveFeelingList(List<PresentationHaveFeeling> presentationHaveFeelingList) {
        this.presentationHaveFeelingList = presentationHaveFeelingList;
    }

    @XmlTransient
    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    @XmlTransient
    public List<Filter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<Filter> filterList) {
        this.filterList = filterList;
    }

    @XmlTransient
    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }
    
    @XmlTransient
    public String getStatus() {
        if(getStartdate()!= null && getEnddate()== null) {
            status = "Started";
        } else if (getStartdate() != null && getEnddate() != null) {
            status = "Finished";
        } else if (getPresentationHaveFeelingList() == null || getPresentationHaveFeelingList().isEmpty()) {
            status = "Configuring";
        } else if (getStartdate() == null && getEnddate() == null) {
            status = "Pending";
        }
        return status;
    }
    
    public Section getOpenSection() {
        for (Section section : sectionList) {
            if(section.getStarttime() != null && section.getEndtime() == null) {
                return section;
            }
        }
        return null;
    }
    
    public boolean isFinalSection() {
        Section open = getOpenSection();
        if(open != null) {
            for (int i = 0; i < sectionList.size(); i++) {
                if (open == sectionList.get(i) && i == sectionList.size()-1) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public boolean getIsStarted() {
        return getStartdate() != null;
    }
    
    public boolean getIsFinished() {
        return getEnddate()!= null;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (presentationId != null ? presentationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Presentation)) {
            return false;
        }
        Presentation other = (Presentation) object;
        if ((this.presentationId == null && other.presentationId != null) || (this.presentationId != null && !this.presentationId.equals(other.presentationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Presentation[ presentationId=" + presentationId + " ]";
    }
    
}
