/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author fran
 */
@Entity
@Table(name = "user_have_property")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserHaveProperty.findAll", query = "SELECT u FROM UserHaveProperty u"),
    @NamedQuery(name = "UserHaveProperty.findByUserId", query = "SELECT u FROM UserHaveProperty u WHERE u.userHavePropertyPK.userId = :userId"),
    @NamedQuery(name = "UserHaveProperty.findByPropertyId", query = "SELECT u FROM UserHaveProperty u WHERE u.userHavePropertyPK.propertyId = :propertyId"),
    @NamedQuery(name = "UserHaveProperty.findByValue", query = "SELECT u FROM UserHaveProperty u WHERE u.value = :value")})
public class UserHaveProperty implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserHavePropertyPK userHavePropertyPK;
    @Size(max = 255)
    private String value;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;
    @JoinColumn(name = "property_id", referencedColumnName = "property_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Property property;
    @Transient
    private DateFormat[] df = {
        new SimpleDateFormat("yyyy-MM-dd"),
        new SimpleDateFormat("dd/MM/yyyy"),
        new SimpleDateFormat("dd/MM/yy")
    };

    public UserHaveProperty() {
    }

    public UserHaveProperty(UserHavePropertyPK userHavePropertyPK) {
        this.userHavePropertyPK = userHavePropertyPK;
    }

    public UserHaveProperty(int userId, int propertyId) {
        this.userHavePropertyPK = new UserHavePropertyPK(userId, propertyId);
    }

    public UserHavePropertyPK getUserHavePropertyPK() {
        return userHavePropertyPK;
    }

    public void setUserHavePropertyPK(UserHavePropertyPK userHavePropertyPK) {
        this.userHavePropertyPK = userHavePropertyPK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getValueAsDate() {
        return getValueAsDate(this.value);
    }

    public Date getValueAsDate(String date) {
        for (DateFormat dateF : df) {
            try {
                return dateF.parse(date);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public void setValueAsDate(Date value) {
        Calendar cal = new GregorianCalendar();
        if (value != null) {
            cal.setTime(value);
            this.value = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH + 1) + "-" + cal.get(Calendar.DATE);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userHavePropertyPK != null ? userHavePropertyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserHaveProperty)) {
            return false;
        }
        UserHaveProperty other = (UserHaveProperty) object;
        if ((this.userHavePropertyPK == null && other.userHavePropertyPK != null) || (this.userHavePropertyPK != null && !this.userHavePropertyPK.equals(other.userHavePropertyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.UserHaveProperty[ userHavePropertyPK=" + userHavePropertyPK + " ]";
    }

}
