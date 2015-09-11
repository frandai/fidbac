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
public class AnoptionPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "presentation_id")
    private int presentationId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "question_id")
    private int questionId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "option_id")
    private int optionId;

    public AnoptionPK() {
    }

    public AnoptionPK(int presentationId, int questionId, int optionId) {
        this.presentationId = presentationId;
        this.questionId = questionId;
        this.optionId = optionId;
    }

    public int getPresentationId() {
        return presentationId;
    }

    public void setPresentationId(int presentationId) {
        this.presentationId = presentationId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) presentationId;
        hash += (int) questionId;
        hash += (int) optionId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnoptionPK)) {
            return false;
        }
        AnoptionPK other = (AnoptionPK) object;
        if (this.presentationId != other.presentationId) {
            return false;
        }
        if (this.questionId != other.questionId) {
            return false;
        }
        if (this.optionId != other.optionId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.AnoptionPK[ presentationId=" + presentationId + ", questionId=" + questionId + ", optionId=" + optionId + " ]";
    }
    
}
