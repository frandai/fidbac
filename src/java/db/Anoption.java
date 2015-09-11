/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
    @NamedQuery(name = "Anoption.findAll", query = "SELECT a FROM Anoption a"),
    @NamedQuery(name = "Anoption.findByPresentationId", query = "SELECT a FROM Anoption a WHERE a.anoptionPK.presentationId = :presentationId"),
    @NamedQuery(name = "Anoption.findByQuestionId", query = "SELECT a FROM Anoption a WHERE a.anoptionPK.questionId = :questionId"),
    @NamedQuery(name = "Anoption.findByOptionId", query = "SELECT a FROM Anoption a WHERE a.anoptionPK.optionId = :optionId"),
    @NamedQuery(name = "Anoption.findByCorrect", query = "SELECT a FROM Anoption a WHERE a.correct = :correct")})
public class Anoption implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AnoptionPK anoptionPK;
    @Lob
    @Size(max = 65535)
    private String anoption;
    @Basic(optional = false)
    @NotNull
    private short correct;
    @JoinTable(name = "answer", joinColumns = {
        @JoinColumn(name = "presentation_id", referencedColumnName = "presentation_id"),
        @JoinColumn(name = "quiestion_id", referencedColumnName = "question_id"),
        @JoinColumn(name = "option_id", referencedColumnName = "option_id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "user_id")})
    @ManyToMany
    private List<User> userList;
    @JoinColumns({
        @JoinColumn(name = "question_id", referencedColumnName = "question_id", insertable = false, updatable = false),
        @JoinColumn(name = "presentation_id", referencedColumnName = "presentation_id", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Question question;
    @Transient
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Anoption() {
    }

    public Anoption(AnoptionPK anoptionPK) {
        this.anoptionPK = anoptionPK;
    }

    public Anoption(AnoptionPK anoptionPK, short correct) {
        this.anoptionPK = anoptionPK;
        this.correct = correct;
    }

    public Anoption(int presentationId, int questionId, int optionId) {
        this.anoptionPK = new AnoptionPK(presentationId, questionId, optionId);
    }

    public AnoptionPK getAnoptionPK() {
        return anoptionPK;
    }

    public void setAnoptionPK(AnoptionPK anoptionPK) {
        this.anoptionPK = anoptionPK;
    }

    public String getAnoption() {
        return anoption;
    }
    
    public String getUserStyle(User user) {
        String style = "";
        if(userList.contains(user)) {
            style = "background-color: " + (getCorrect()==1? "lightgreen" : "darksalmon");
        }
        return style;
    }

    public void setAnoption(String anoption) {
        this.anoption = anoption;
    }

    public short getCorrect() {
        return correct;
    }

    public void setCorrect(short correct) {
        this.correct = correct;
    }

    @XmlTransient
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (anoptionPK != null ? anoptionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Anoption)) {
            return false;
        }
        Anoption other = (Anoption) object;
        if ((this.anoptionPK == null && other.anoptionPK != null) || (this.anoptionPK != null && !this.anoptionPK.equals(other.anoptionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Anoption[ anoptionPK=" + anoptionPK + " ]";
    }
    
}
