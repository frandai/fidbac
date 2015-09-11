package controller;

import db.Anoption;
import db.Filter;
import db.Presentation;
import db.PresentationHaveFeeling;
import db.Property;
import db.Question;
import db.User;
import db.UserHaveProperty;
import events.EventsUtilities;
import events.answer.AnswerEvent;
import events.feelings.FeelingEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import mdb.utilMongoDB;

@ManagedBean(name = "client")
@SessionScoped
public class Client implements Serializable {

    @ManagedProperty(value = "#{mongoDb}")
    private utilMongoDB mongoDB;
    private Presentation selectedPresentation;
    private List<UserHaveProperty> unknownPropertyList;
    private User virtualUser;
    private int selectedFeelingId = -1;
    private String answerMessage;
    private String errorMessage;
    private String askMessage;

    public String getAskMessage() {
        return askMessage;
    }

    public void setAskMessage(String askMessage) {
        this.askMessage = askMessage;
    }

    public void sendAsk() {
        AnswerEvent a = new AnswerEvent();
        a.setText(askMessage);
        EventsUtilities.publishAnswer(
                selectedPresentation.getPresentationId().toString(),
                a);
        askMessage = "";
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public utilMongoDB getMongoDB() {
        return mongoDB;
    }

    public void setMongoDB(utilMongoDB mongoDB) {
        this.mongoDB = mongoDB;
    }

    public String getAnswerMessage() {
        return answerMessage;
    }

    public void setAnswerMessage(String answerMessage) {
        this.answerMessage = answerMessage;
    }

    public int getSelectedFeelingId() {
        return selectedFeelingId;
    }

    public void setSelectedFeelingId(int selectedFeelingId) {
        if (selectedFeelingId != this.selectedFeelingId) {
            processFeeling(selectedFeelingId, this.selectedFeelingId);
        }
        this.selectedFeelingId = selectedFeelingId;
    }

    public User getVirtualUser() {
        return virtualUser;
    }

    public void setVirtualUser(User virtualUser) {
        this.virtualUser = virtualUser;
    }

    public List<UserHaveProperty> getUnknownPropertyList() {
        if (unknownPropertyList == null && getSelectedPresentation() != null) {
            unknownPropertyList = new ArrayList<>();

            List<Property> presentationPropertyList = getSelectedPresentation().getPropertyList();
            for (Property p : presentationPropertyList) {
                if (!userHaveProperty(p)) {
                    UserHaveProperty prop = new UserHaveProperty(getVirtualUser().getUserId(), p.getPropertyId());
                    prop.setProperty(p);
                    prop.setUser(getVirtualUser());
                    prop.setValue("");
                    getVirtualUser().getUserHavePropertyList().add(prop);
                    unknownPropertyList.add(prop);
                }
            }
        }
        return unknownPropertyList;
    }

    @EJB
    private beans.PresentationFacade presentationFacade;
    @EJB
    private beans.UserFacade userFacade;
    @EJB
    private beans.AnoptionFacade anoptionFacade;

    public Presentation getSelectedPresentation() {
        this.setErrorMessage("");
        String code = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code");
        if (code != null && selectedPresentation != null && !selectedPresentation.getCode().equals(code)) {
            selectedPresentation = null;
            unknownPropertyList = null;
            virtualUser = null;
            answerMessage = null;
        }
        if (selectedPresentation == null) {
            try {
                if (code == null || code.isEmpty()) {
                    throw new Exception("The presentation code is mandatory.");
                }
                code = code.trim();
                selectedPresentation = presentationFacade.find(code);
                if (selectedPresentation == null) {
                    throw new Exception("The presentation '" + code + "' does not exist.");
                } else if (selectedPresentation.getStartdate() == null) {
                    throw new Exception("The presentation '" + code + "' have not started.");
                } else if (selectedPresentation.getEnddate() != null) {
                    throw new Exception("The presentation '" + code + "' have finished.");
                }
            } catch (Exception e) {
                selectedPresentation = null;
                this.setErrorMessage(e.getMessage());
                util.Utilities.redirect("index.xhtml");
                return null;
            }

            for (PresentationHaveFeeling f : selectedPresentation.getPresentationHaveFeelingList()) {
                if (f.getIsdefault() == 1) {
                    selectedFeelingId = f.getFeeling().getFeelingId();
                }
            }
        }
        return selectedPresentation;
    }

    public String getFeelingCSS(int feelingId) {
        if (selectedFeelingId == feelingId) {
            String color = "#F3F3F3";
            for (PresentationHaveFeeling f : selectedPresentation.getPresentationHaveFeelingList()) {
                if (f.getFeeling().getFeelingId() == selectedFeelingId) {
                    color = f.getFeeling().getColor();
                }
            }
            return "background: -webkit-linear-gradient(#F3F3F3, " + color + "); /* For Safari 5.1 to 6.0 */\n"
                    + "background: -o-linear-gradient(#F3F3F3, " + color + "); /* For Opera 11.1 to 12.0 */\n"
                    + "background: -moz-linear-gradient(#F3F3F3, " + color + "); /* For Firefox 3.6 to 15 */\n"
                    + "background: linear-gradient(#F3F3F3, " + color + ");";
        } else {
            return "";
        }
    }

    private boolean userHaveProperty(Property p) {
        for (UserHaveProperty userP : getVirtualUser().getUserHavePropertyList()) {
            if (userP.getProperty().getPropertyId().equals(p.getPropertyId())
                    && userP.getValue() != null && !userP.getValue().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public String propertyOk() {
        boolean porpertyok = true;
        for (UserHaveProperty p : getUnknownPropertyList()) {
            if (p.getValue() == null || p.getValue().isEmpty()) {
                util.Utilities.showMessage(p.getProperty().getDescription() + ": mandatory information.", true);
                porpertyok = false;
            }
        }
        if (!porpertyok) {
            return "";
        }

        if (getVirtualUser().getUserId() > 0 && !getVirtualUser().getPresentationList().contains(selectedPresentation)) {
            getVirtualUser().getPresentationList().add(selectedPresentation);
            selectedPresentation.getUserList().add(getVirtualUser());
            userFacade.edit(getVirtualUser());
        }

        if (!in) {
            processFeeling(selectedFeelingId, -1);
            in = true;
        }

        return "clientfeeling";
    }

    public void processQuestion(Question question) {
        if (getVirtualUser().getUserId() > 0) {
            for (Anoption option : question.getAnoptionList()) {
                if (getVirtualUser().getAnoptionList().contains(option) && !option.isSelected()) {
                    getVirtualUser().getAnoptionList().remove(option);
                    option.getUserList().remove(getVirtualUser());
                }
                if (!getVirtualUser().getAnoptionList().contains(option) && option.isSelected()) {
                    getVirtualUser().getAnoptionList().add(option);
                    option.getUserList().add(getVirtualUser());
                }
                anoptionFacade.edit(option);
            }
        }
        ArrayList<String> selectedOptions = new ArrayList<>();
        boolean correct = true;
        for (Anoption option : question.getAnoptionList()) {
            if (option.isSelected() && option.getCorrect() == 0) {
                correct = false;
            }
            if (!option.isSelected() && option.getCorrect() == 1) {
                correct = false;
            }
            if (option.isSelected()) {
                selectedOptions.add(option.getAnoption());
            }
        }
        answerMessage = "You answered the question \"" + question.getQuestion() + "\" <strong>" + (correct ? "Correctly" : "Incorrectly") + "</strong>";

        String[] options = selectedOptions.toArray(new String[selectedOptions.size()]);
        EventsUtilities.publishAnswer(
                selectedPresentation.getPresentationId().toString(),
                new AnswerEvent(options, correct));
    }

    private boolean in = false;

    public String exit() {
        if (selectedPresentation != null) {
            if (in) {
                processFeeling(-1, selectedFeelingId);
                in = false;
            }
        }
        return "client";
    }

    private void processFeeling(int currentFeelingId, int previousFeelingId) {
        EventsUtilities.publishFeeling(selectedPresentation.getPresentationId().toString(),
                new FeelingEvent(currentFeelingId, previousFeelingId, "general", getVirtualUser().getUserId())
        );
        for (Filter f : selectedPresentation.getFilterList()) {
            if (f.accepts(unknownPropertyList) || f.accepts(getVirtualUser().getUserHavePropertyList())) {
                EventsUtilities.publishFeeling(selectedPresentation.getPresentationId().toString(),
                        new FeelingEvent(currentFeelingId, previousFeelingId, "filter" + f.getFilterPK().getFilterId(), getVirtualUser().getUserId())
                );
            }
        }

        ArrayList totalPropertyList = new ArrayList();
        totalPropertyList.addAll(unknownPropertyList);
        totalPropertyList.addAll(getVirtualUser().getUserHavePropertyList());

        getMongoDB().insertFeeling(selectedPresentation.getPresentationId(),
                getVirtualUser().getUserId(), currentFeelingId, previousFeelingId,
                totalPropertyList);
    }

    void updatePresentationFeelings() {
        if (selectedPresentation != null) {
            if (!in) {
                processFeeling(selectedFeelingId, -1);
                in = true;
            }
        }
    }
}
