/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
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
    @NamedQuery(name = "Question.findAll", query = "SELECT q FROM Question q"),
    @NamedQuery(name = "Question.findByPresentationId", query = "SELECT q FROM Question q WHERE q.questionPK.presentationId = :presentationId"),
    @NamedQuery(name = "Question.findByQuestionId", query = "SELECT q FROM Question q WHERE q.questionPK.questionId = :questionId")})
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected QuestionPK questionPK;
    @Lob
    @Size(max = 65535)
    private String question;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    private List<Anoption> anoptionList;
    @JoinColumn(name = "presentation_id", referencedColumnName = "presentation_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Presentation presentation;
    @Transient
    private String optionsSemicolon;

    public Question() {
    }

    public Question(QuestionPK questionPK) {
        this.questionPK = questionPK;
    }

    public Question(int presentationId, int questionId) {
        this.questionPK = new QuestionPK(presentationId, questionId);
    }

    public QuestionPK getQuestionPK() {
        return questionPK;
    }

    public void setQuestionPK(QuestionPK questionPK) {
        this.questionPK = questionPK;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @XmlTransient
    public List<Anoption> getAnoptionList() {
        return anoptionList;
    }

    public void setAnoptionList(List<Anoption> anoptionList) {
        this.anoptionList = anoptionList;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }
    
    public String getOptionsHTML() {
        String html = "<ul style='list-style-type: none;padding-left: 0px;'>";
        for (Anoption option : anoptionList) {
            html += "<li style='"+((option.getCorrect() == 1) ? "font-weight: bolder;" : "")+"'>"+option.getAnoption()+"</li>";
        }
        html += "</ul>";
        return html;
    }
    
    public String getOptionsSemicolon() {
        if(optionsSemicolon == null) {
            String options = "";
            for (Anoption option : anoptionList) {
                options += ";\n"+option.getAnoption()+((option.getCorrect() == 1) ? "*" : "");
            }
            optionsSemicolon = options.replaceFirst(";\n", "");
        }
        return optionsSemicolon;
    }

    public void setOptionsSemicolon(String optionsSemicolon) {
        this.optionsSemicolon = optionsSemicolon;
    }
    
    public String answerMessage(User user) {
        boolean correct = true;
        for (Anoption option : getAnoptionList()) {
            if (option.getUserList().contains(user) && option.getCorrect() == 0) {
                correct = false;
            }
            if (!option.isSelected() && option.getCorrect() == 1) {
                correct = false;
            }
        }
        return "You answered the question <strong>" + (correct ? "Correctly" : "Incorrectly") + "</strong>";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (questionPK != null ? questionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.questionPK == null && other.questionPK != null) || (this.questionPK != null && !this.questionPK.equals(other.questionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Question[ questionPK=" + questionPK + " ]";
    }
    
}
