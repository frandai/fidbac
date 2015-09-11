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
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUserId", query = "SELECT u FROM User u WHERE u.userId = :userId"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String password;
    @JoinTable(name = "answer", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "user_id")}, inverseJoinColumns = {
            @JoinColumn(name = "presentation_id", referencedColumnName = "presentation_id"),
            @JoinColumn(name = "quiestion_id", referencedColumnName = "question_id"),
            @JoinColumn(name = "option_id", referencedColumnName = "option_id")
        })
    @ManyToMany
    private List<Anoption> anoptionList;
    @JoinTable(name = "user_participates_presentation", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "user_id")}, inverseJoinColumns = {
        @JoinColumn(name = "presentation_id", referencedColumnName = "presentation_id")})
    @ManyToMany
    private List<Presentation> presentationList;
    @ManyToMany(mappedBy = "userListAdmin")
    private List<Presentation> presentationListAdmin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserHaveProperty> userHavePropertyList;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(Integer userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlTransient
    public List<Anoption> getAnoptionList() {
        return anoptionList;
    }

    public void setAnoptionList(List<Anoption> anoptionList) {
        this.anoptionList = anoptionList;
    }

    @XmlTransient
    public List<Presentation> getPresentationList() {
        return presentationList;
    }

    public void setPresentationList(List<Presentation> presentationList) {
        this.presentationList = presentationList;
    }

    @XmlTransient
    public List<Presentation> getPresentationListAdmin() {
        return presentationListAdmin;
    }

    public void setPresentationListAdmin(List<Presentation> presentationListAdmin) {
        this.presentationListAdmin = presentationListAdmin;
    }

    @XmlTransient
    public List<UserHaveProperty> getUserHavePropertyList() {
        return userHavePropertyList;
    }

    public void setUserHavePropertyList(List<UserHaveProperty> userHavePropertyList) {
        this.userHavePropertyList = userHavePropertyList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.User[ userId=" + userId + " ]";
    }
    
}
