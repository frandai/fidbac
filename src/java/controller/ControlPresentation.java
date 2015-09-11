package controller;

import db.Anoption;
import db.Filter;
import db.Presentation;
import db.Property;
import db.Question;
import db.Section;
import db.UserHaveProperty;
import events.EventsUtilities;
import events.question.QuestionEvent;
import events.section.SectionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import mdb.SimpleFeeling;
import mdb.SimpleProperty;
import mdb.utilMongoDB;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "controlPresentation")
@ViewScoped
public class ControlPresentation implements Serializable {

    @ManagedProperty(value = "#{loggedSession}")
    private LoggedSession loggedSession;
    @ManagedProperty(value = "#{presentations}")
    private Presentations presentations;
    @ManagedProperty(value = "#{mongoDb}")
    private utilMongoDB mongoDB;
    private Question selectedQuestion;
    private boolean showCloseQuestionButton;
    private List<SimpleFeeling> feelingsData;
    private HashMap<Integer, Property> propertiresData;
    
    @EJB
    beans.PropertyFacade propertyFacade;

    public long getCurrentDate() {
        return (new Date()).getTime();
    }
    
    public utilMongoDB getMongoDB() {
        return mongoDB;
    }

    public void setMongoDB(utilMongoDB mongoDB) {
        this.mongoDB = mongoDB;
    }

    public Question getSelectedQuestion() {
        return selectedQuestion;
    }

    public void setSelectedQuestion(Question selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }

    public Presentations getPresentations() {
        return presentations;
    }

    public void setPresentations(Presentations presentations) {
        this.presentations = presentations;
    }

    public LoggedSession getLoggedSession() {
        return loggedSession;
    }

    public void setLoggedSession(LoggedSession loggedSession) {
        this.loggedSession = loggedSession;
    }

    public void nextSection() {
        Presentation presentation = loggedSession.getAdminPresentation();
        Section open = presentation.getOpenSection();
        String nextSectionTitle = "...EndOfPresentaton...";
        String nextSectionDescription = "";
        boolean changeSection = false;
        if (open != null) {
            open.setEndtime(new Date());
            List<Section> sectionList = presentation.getSectionList();
            for (Section section : sectionList) {
                if (section.getStarttime() == null) {
                    section.setStarttime(new Date());
                    nextSectionTitle = section.getTitle();
                    nextSectionDescription = section.getDescription();
                    changeSection = true;
                    break;
                }
            }
        }
        if (presentation.isFinalSection() && !changeSection) {
            presentation.setEnddate(new Date());
        }

        presentations.savePresentation();

        EventsUtilities.publishSection(presentation.getPresentationId().toString(), new SectionEvent(nextSectionTitle, nextSectionDescription));
    }

    public void openQuestion(Question question) {
        showCloseQuestionButton = true;
        selectedQuestion = question;
        Presentation presentation = loggedSession.getAdminPresentation();
        EventsUtilities.publishQuestion(presentation.getPresentationId().toString(), new QuestionEvent(question.getQuestionPK().getQuestionId(), true));
        String options = "";
        for (Anoption option : question.getAnoptionList()) {
            options += ",'"+option.getAnoption().replace('\'', '\0')+"'";
        }
        options = options.replaceFirst(",", "");
        String exec = "showAnswers(["
                + options
                + "]);";
        RequestContext.getCurrentInstance().execute(exec);
    }

    public void closeQuestion() {
        Presentation presentation = loggedSession.getAdminPresentation();
        EventsUtilities.publishQuestion(presentation.getPresentationId().toString(), new QuestionEvent(selectedQuestion.getQuestionPK().getQuestionId(), false));
        RequestContext.getCurrentInstance().execute("alert('Questions will be closed in 10 seconds. The results dialog will remain open.');");
        showCloseQuestionButton = false;
    }

    public boolean isShowCloseQuestionButton() {
        return showCloseQuestionButton;
    }

    public void setShowCloseQuestionButton(boolean showCloseQuestionButton) {
        this.showCloseQuestionButton = showCloseQuestionButton;
    }
    
    public String getGeneralData() {
        long startDate = loggedSession.getAdminPresentation().getStartdate().getTime();
        
        feelingsData = mongoDB.getLoadDataForPresentation((int) loggedSession.getAdminPresentation().getPresentationId());
        
        return buildData(feelingsData, startDate);
    }
    
    public String getUserData() {
        long startDate = loggedSession.getViewPresentation().getStartdate().getTime();
        
        feelingsData = mongoDB.getFeelingsForUser((int) loggedSession.getViewPresentation().getPresentationId(),
                loggedSession.getLogUser().getUserId());
        
        return buildData(feelingsData, startDate);
    }

    private String getMinutes(long startDate, long time) {
        return ""+((time - startDate) / (1000.0 * 60.0));
    }
    
    private String buildData(List<SimpleFeeling> feelingsData, long startDate) {
        String generalFeelingsData = "[";
        for (SimpleFeeling feeling : feelingsData) {
            generalFeelingsData += (generalFeelingsData.equals("[") ? "" : ",") +
                    "{ userId: "+feeling.getUserId()+", time: "+getMinutes(startDate,feeling.getTime())+", currentFeeling: "+feeling.getFeelingId()+", previousFeeling: "+feeling.getPreviousFeelingId()+" }";
        }
        generalFeelingsData += "]";
        
        System.out.println(generalFeelingsData);
        return generalFeelingsData;
    }
    
    public String dataFilter(Filter filter) {
        long startDate = loggedSession.getAdminPresentation().getStartdate().getTime();
        List<SimpleFeeling> filteredFeelingsData = new ArrayList<>();
        
        for (SimpleFeeling feeling : feelingsData) {
            if (filter.accepts(getPropertyList(feeling))) {
                filteredFeelingsData.add(feeling);
            }
        }
        
        return buildData(filteredFeelingsData, startDate);
    }

    private List<UserHaveProperty> getPropertyList(SimpleFeeling feeling) {
        List<UserHaveProperty> feelingProp = new ArrayList<>();
        if(propertiresData == null) {
            propertiresData = new HashMap<>();
        }
        for (SimpleProperty property : feeling.getPropertires()) {
            UserHaveProperty up = new UserHaveProperty(feeling.getUserId(), property.getPropertireId());
            Property selectedPropery = propertiresData.get(property.getPropertireId());
            up.setValue(property.getPropertireValue());
            
            if(selectedPropery == null) {
                up.setProperty(propertyFacade.find(property.getPropertireId()));
                propertiresData.put(property.getPropertireId(), up.getProperty());
            } else {
                up.setProperty(selectedPropery);
            }
            feelingProp.add(up);
        }
            
        return feelingProp;
    }
}
