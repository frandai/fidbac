package controller;

import db.Anoption;
import db.Feeling;
import db.Filter;
import db.Presentation;
import db.PresentationHaveFeeling;
import db.Property;
import db.Question;
import db.Section;
import db.Value;
import db.ValuePK;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import mdb.utilMongoDB;

@ManagedBean(name = "adminPresentation")
@ViewScoped
public class AdminPresentation implements Serializable {

    @ManagedProperty(value = "#{loggedSession}")
    private LoggedSession loggedSession;
    @ManagedProperty(value = "#{presentations}")
    private Presentations presentations;
    @ManagedProperty(value = "#{mongoDb}")
    private utilMongoDB mongoDB;

    public utilMongoDB getMongoDB() {
        return mongoDB;
    }

    public void setMongoDB(utilMongoDB mongoDB) {
        this.mongoDB = mongoDB;
    }

    private List<Property> defaultPropertires;
    private String propertyDescription = "";
    private int propertyType = 1;
    private String propertyValues = "";
    private PresentationHaveFeeling selectedFeeling;

    public PresentationHaveFeeling getSelectedFeeling() {
        return selectedFeeling;
    }

    public void setSelectedFeeling(PresentationHaveFeeling selectedFeeling) {
        this.selectedFeeling = selectedFeeling;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public int getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(int propertyType) {
        this.propertyType = propertyType;
    }

    @EJB
    private beans.PropertyFacade propertyFacade;

    @EJB
    private beans.FeelingFacade feelingFacade;

    @EJB
    private beans.PresentationFacade presentationFacade;

    @EJB
    private beans.PresentationHaveFeelingFacade presentationHaveFeelingFacade;

    @EJB
    private beans.SectionFacade sectionFacade;

    @EJB
    private beans.QuestionFacade questionFacade;

    @EJB
    private beans.AnoptionFacade optionFacade;

    public String getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(String propertyValues) {
        this.propertyValues = propertyValues;
    }

    public LoggedSession getLoggedSession() {
        return loggedSession;
    }

    public void setLoggedSession(LoggedSession loggedSession) {
        this.loggedSession = loggedSession;
    }

    public Presentations getPresentations() {
        return presentations;
    }

    public void setPresentations(Presentations presentations) {
        this.presentations = presentations;
    }

    public String startPresentation() {
        if (loggedSession.getAdminPresentation().getStartdate() == null) {
            loggedSession.getAdminPresentation().setStartdate(new Date());
            if (!loggedSession.getAdminPresentation().getSectionList().isEmpty()
                    && loggedSession.getAdminPresentation().getSectionList().get(0).getStarttime() == null) {
                loggedSession.getAdminPresentation().getSectionList().get(0).setStarttime(new Date());
            }
        }
        presentations.savePresentation();
        return "controlpresentation";
    }

    /**
     * * FEELINGS **
     */
    public String saveFeeling() {
        List<Feeling> defaultFeelings = feelingFacade.findDefaultFeelings();
        Feeling selF = selectedFeeling.getFeeling();
        for (Feeling defaultFeeling : defaultFeelings) {
            if (Objects.equals(defaultFeeling.getFeelingId(), selF.getFeelingId())
                    && (!defaultFeeling.getName().equals(selF.getName())
                    || !defaultFeeling.getColor().equals(selF.getColor())
                    || !defaultFeeling.getIcon().equals(selF.getIcon()))) {
                deleteFeeling(selectedFeeling, false);
                addFeeling();
                PresentationHaveFeeling fnew = loggedSession.getAdminPresentation().getPresentationHaveFeelingList()
                        .get(loggedSession.getAdminPresentation().getPresentationHaveFeelingList().size() - 1);

                fnew.setOrder(selectedFeeling.getOrder());
                fnew.setIsdefault(selectedFeeling.getIsdefault());
                fnew.getFeeling().setColor(selF.getColor());
                fnew.getFeeling().setName(selF.getName());
                fnew.getFeeling().setIcon(selF.getIcon());
                selectedFeeling = fnew;
                selF = fnew.getFeeling();
            }
        }
        selectedFeeling.setFeeling(feelingFacade.edit(selF));
        presentationHaveFeelingFacade.edit(selectedFeeling);

        loggedSession.setAdminPresentation(presentationFacade.find(loggedSession.getAdminPresentation().getPresentationId()));
        return "adminpresentationfeelings";
    }

    public String addFeeling() {
        Presentation presentation = loggedSession.getAdminPresentation();
        Feeling feeling = new Feeling(null, "New Feeling", "#c9c9c9", "5");
        feeling = feelingFacade.create(feeling);
        PresentationHaveFeeling f = new PresentationHaveFeeling(presentation.getPresentationId(), feeling.getFeelingId());
        f.setOrder(presentation.getPresentationHaveFeelingList().size() + 1);
        f.setFeeling(feeling);
        loggedSession.getAdminPresentation().getPresentationHaveFeelingList().add(f);
        presentations.savePresentation();
        return "adminpresentationfeelings";
    }

    public String deleteFeeling(PresentationHaveFeeling feeling) {
        return deleteFeeling(feeling, true);
    }

    private String deleteFeeling(PresentationHaveFeeling feeling, boolean order) {
        loggedSession.getAdminPresentation().getPresentationHaveFeelingList().remove(feeling);
        if (order) {
            int i = 1;
            boolean defaultSet = false;
            for (PresentationHaveFeeling f : loggedSession.getAdminPresentation().getPresentationHaveFeelingList()) {
                f.setOrder(i);
                if (f.getIsdefault() == 1) {
                    defaultSet = true;
                }
                ++i;
            }
            if (!defaultSet && !loggedSession.getAdminPresentation().getPresentationHaveFeelingList().isEmpty()) {
                loggedSession.getAdminPresentation().getPresentationHaveFeelingList().get((i - 1) / 2).setIsdefault((short) 1);
            }
        }
        presentationHaveFeelingFacade.remove(feeling);
        presentations.savePresentation();
        if (feeling.getFeeling().getFeelingId() > 5) {
            feelingFacade.remove(feeling.getFeeling());
        }
        return "adminpresentationfeelings";
    }

    public String upFeeling(PresentationHaveFeeling feeling) {
        List<PresentationHaveFeeling> list = loggedSession.getAdminPresentation().getPresentationHaveFeelingList();
        PresentationHaveFeeling feelingup = list.get(list.indexOf(feeling));
        PresentationHaveFeeling feelingdown = list.get(list.indexOf(feeling) - 1);
        feelingup.setOrder(feelingup.getOrder() - 1);
        feelingdown.setOrder(feelingdown.getOrder() + 1);
        loggedSession.getAdminPresentation().setPresentationHaveFeelingList(list);
        presentations.savePresentation();
        return "adminpresentationfeelings";
    }

    /**
     * * END: FEELINGS **
     */
    /**
     * * USER DATA **
     */
    public List<Property> getDefaultPropertires() {
        if (defaultPropertires == null) {
            defaultPropertires = propertyFacade.getDefaultPropertires();
            for (Property property : loggedSession.getAdminPresentation().getPropertyList()) {
                if (defaultPropertires.contains(property)) {
                    defaultPropertires.get(defaultPropertires.indexOf(property)).setPropertySelected(true);
                }
            }
        }
        return defaultPropertires;
    }

    public String saveUserData() {
        ArrayList<Property> selectedPropertires = new ArrayList<>();
        for (Property selectedPropertire : defaultPropertires) {
            if (selectedPropertire.isPropertySelected()) {
                selectedPropertires.add(selectedPropertire);
            }
        }
        selectedPropertires.addAll(loggedSession.getAdminPresentation().getSelectedNotDefaultPropertyList());
        loggedSession.getAdminPresentation().setPropertyList(selectedPropertires);
        return presentations.savePresentation();
    }

    public void changeProperty(Property p) {
        p.setPropertySelected(p.isPropertySelected());
        Property rp = null;
        for (Property pl : loggedSession.getAdminPresentation().getPropertyList()) {
            if (pl.getPropertyId() == p.getPropertyId()) {
                rp = pl;
            }
        }
        if (rp != null) {
            loggedSession.getAdminPresentation().getPropertyList().remove(rp);
        } 
        presentations.savePresentation();
    }

    public String saveProperty() {
        Property selectedProperty = new Property();

        selectedProperty.setDescription(propertyDescription);
        selectedProperty.setType(propertyType);

        selectedProperty = propertyFacade.create(selectedProperty);
        if (selectedProperty.getType() == 2) {
            ArrayList<Value> values = new ArrayList<>();
            int i = 1;
            for (String propValue : propertyValues.split(";")) {
                propValue = propValue.trim();
                values.add(new Value(new ValuePK(
                        i, selectedProperty.getPropertyId()), propValue));
                ++i;
            }
            selectedProperty.setValueList(values);
            selectedProperty = propertyFacade.edit(selectedProperty);
        }
        loggedSession.getAdminPresentation().getPropertyList().add(selectedProperty);

        propertyDescription = "";
        propertyType = 1;
        propertyValues = "";
        return "adminpresentationuserdata";
    }

    /**
     * * END: USER DATA **
     */
    /**
     * * SECTIONS **
     */
    private Section selectedSection;

    public Section getSelectedSection() {
        return selectedSection;
    }

    public void setSelectedSection(Section selectedSection) {
        this.selectedSection = selectedSection;
    }

    public String deleteSection(Section section) {
        sectionFacade.remove(section);
        loggedSession.getAdminPresentation().getSectionList().remove(section);
        presentations.savePresentation();
        return "adminpresentationsections";
    }

    public void addSection() {
        int nextSection = 1;
        for (Section s : loggedSession.getAdminPresentation().getSectionList()) {
            nextSection = (s.getSectionPK().getSectionId() >= nextSection) ? s.getSectionPK().getSectionId() + 1 : nextSection;
        }
        Section section = new Section(
                loggedSession.getAdminPresentation().getPresentationId(),
                nextSection);
        section.setTitle("New Section");
        section.setDescription("");
        loggedSession.getAdminPresentation().getSectionList().add(section);
        presentations.savePresentation();
        for (Section s : loggedSession.getAdminPresentation().getSectionList()) {
            if (s.getSectionPK().getSectionId() == nextSection) {
                selectedSection = s;
            }
        }
    }

    public String deletePresentation() {
        int presentationId = loggedSession.getAdminPresentation().getPresentationId();
        presentationFacade.remove(loggedSession.getAdminPresentation());
        loggedSession.setAdminPresentation(null);
        mongoDB.removePresentation(presentationId);
        return "mypresentations";
    }

    public String saveSection() {
        presentations.savePresentation();
        return "adminpresentationsections";
    }

    /**
     * * END: SECTIONS **
     */
    /**
     * * QUESTIONS **
     */
    private Question selectedQuestion;

    public Question getSelectedQuestion() {
        return selectedQuestion;
    }

    public void setSelectedQuestion(Question selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }

    public String deleteQuestion(Question question) {
        questionFacade.remove(question);
        loggedSession.getAdminPresentation().getQuestionList().remove(question);
        presentations.savePresentation();
        return "adminpresentationquestions";
    }

    public void addQuestion() {
        int nextQuestion = 1;
        for (Question q : loggedSession.getAdminPresentation().getQuestionList()) {
            nextQuestion = (q.getQuestionPK().getQuestionId() >= nextQuestion) ? q.getQuestionPK().getQuestionId() + 1 : nextQuestion;
        }
        Question question = new Question(
                loggedSession.getAdminPresentation().getPresentationId(),
                nextQuestion);
        question.setQuestion("New Question");
        question.setAnoptionList(new ArrayList<Anoption>());
        Anoption optionYes = new Anoption(loggedSession.getAdminPresentation().getPresentationId(), nextQuestion, 1);
        Anoption optionNo = new Anoption(loggedSession.getAdminPresentation().getPresentationId(), nextQuestion, 2);
        optionYes.setCorrect((short) 1);
        optionNo.setCorrect((short) 0);
        optionYes.setAnoption("Yes");
        optionNo.setAnoption("No");
        question.getAnoptionList().add(optionYes);
        question.getAnoptionList().add(optionNo);
        loggedSession.getAdminPresentation().getQuestionList().add(question);
        presentations.savePresentation();
        for (Question q : loggedSession.getAdminPresentation().getQuestionList()) {
            if (q.getQuestionPK().getQuestionId() == nextQuestion) {
                selectedQuestion = q;
            }
        }
    }

    public String saveQuestion() {
        if (!selectedQuestion.getOptionsSemicolon().contains("*")) {
            util.Utilities.showMessage("Questions must have at least one correct answer (marked with '*'). Changes not saved.", true);
            return "adminpresentationquestions";
        }
        for (Anoption op : selectedQuestion.getAnoptionList()) {
            optionFacade.remove(op);
        }
        ArrayList<Anoption> options = new ArrayList<>();
        int i = 1;
        for (String option : selectedQuestion.getOptionsSemicolon().split(";")) {
            option = option.trim();
            if (!option.isEmpty()) {
                boolean isCorrect = option.contains("*");
                Anoption theOption = new Anoption(loggedSession.getAdminPresentation().getPresentationId(),
                        selectedQuestion.getQuestionPK().getQuestionId(),
                        i);
                theOption.setCorrect(isCorrect ? (short) 1 : (short) 0);
                theOption.setAnoption(option.replaceAll("\\*", ""));
                options.add(theOption);
                ++i;
            }
        }
        selectedQuestion.setAnoptionList(options);
        presentations.savePresentation();
        return "adminpresentationquestions";
    }
    /**
     * * END: QUESTIONS **
     */

    /**
     * * FILTERS **
     */
    private Filter selectedFilter;

    @EJB
    private beans.FilterFacade filterFacade;

    public Filter getSelectedFilter() {
        if (selectedFilter == null) {
            selectedFilter = new Filter();
        }
        return selectedFilter;
    }

    public void setSelectedFilter(Filter selectedFilter) {
        this.selectedFilter = selectedFilter;
    }

    public String deleteFilter(Filter filter) {
        filterFacade.remove(filter);
        loggedSession.getAdminPresentation().getFilterList().remove(filter);
        presentations.savePresentation();
        return "adminpresentationfilters";
    }

    public void addFilter() {
        int nextFilter = 1;
        for (Filter f : loggedSession.getAdminPresentation().getFilterList()) {
            nextFilter = (f.getFilterPK().getFilterId() >= nextFilter) ? f.getFilterPK().getFilterId() + 1 : nextFilter;
        }
        Filter filter = new Filter(
                loggedSession.getAdminPresentation().getPresentationId(),
                nextFilter);
        filter.setOperation("eq");
        filter.setPresentation(loggedSession.getAdminPresentation());
        filter.setValue("nothing");
        filter.setPropertyId(loggedSession.getAdminPresentation().getPropertyList().get(0));

        loggedSession.getAdminPresentation().getFilterList().add(filter);
        presentations.savePresentation();
        for (Filter f : loggedSession.getAdminPresentation().getFilterList()) {
            if (f.getFilterPK().getFilterId() == nextFilter) {
                selectedFilter = f;
            }
        }
    }

    public String saveFilter() {
        presentations.savePresentation();
        return "adminpresentationfilters";
    }

    /**
     * * END: FILTERS **
     */
}
