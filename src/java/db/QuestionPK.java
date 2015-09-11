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
public class QuestionPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "presentation_id")
    private int presentationId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "question_id")
    private int questionId;

    public QuestionPK() {
    }

    public QuestionPK(int presentationId, int questionId) {
        this.presentationId = presentationId;
        this.questionId = questionId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) presentationId;
        hash += (int) questionId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuestionPK)) {
            return false;
        }
        QuestionPK other = (QuestionPK) object;
        if (this.presentationId != other.presentationId) {
            return false;
        }
        if (this.questionId != other.questionId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.QuestionPK[ presentationId=" + presentationId + ", questionId=" + questionId + " ]";
    }
    
}
