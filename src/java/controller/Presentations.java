package controller;

import db.Feeling;
import db.Filter;
import db.Presentation;
import db.PresentationHaveFeeling;
import db.Property;
import db.Question;
import db.Section;
import db.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "presentations")
@ViewScoped
public class Presentations implements Serializable {

    @ManagedProperty(value = "#{loggedSession}")
    private LoggedSession loggedSession;

    @EJB
    private beans.PresentationFacade presentationFacade;
    @EJB
    private beans.UserFacade userFacade;
    @EJB
    private beans.FeelingFacade feelingFacade;

    private User userProfile;
    private String newPresentationCode, newPresentationTitle, newPresentationDescription;
    private Random r = new Random();
    private String newCode;

    public String getNewCode() {
        return newCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }

    public String getNewPresentationTitle() {
        return newPresentationTitle;
    }

    public void setNewPresentationTitle(String newPresentationTitle) {
        this.newPresentationTitle = newPresentationTitle;
    }

    public String getNewPresentationDescription() {
        return newPresentationDescription;
    }

    public void setNewPresentationDescription(String newPresentationDescription) {
        this.newPresentationDescription = newPresentationDescription;
    }

    public String getNewPresentationCode() {
        return newPresentationCode;
    }

    public void setNewPresentationCode(String newPresentationCode) {
        this.newPresentationCode = newPresentationCode;
    }

    public LoggedSession getLoggedSession() {
        return loggedSession;
    }

    public void setLoggedSession(LoggedSession loggedSession) {
        this.loggedSession = loggedSession;
    }

    public User getUserProfile() {
        if (userProfile == null) {
            userProfile = getLoggedSession().getLogUser();
            if (userProfile == null) {
                util.Utilities.redirect("index.xhtml");
            } else {
                userProfile = userFacade.find(userProfile.getUserId());
            }
        }
        return userProfile;
    }

    public String newPresentation() {
        if (!newPresentationCode.trim().isEmpty()) {
            if (presentationFacade.find(newPresentationCode.trim()) != null) {
                util.Utilities.showMessage("The presentation code already exists.", true);
                return "";
            }
        } else {
            do {
                newPresentationCode = generateRandomCode();
            } while (presentationFacade.find(newPresentationCode.trim()) != null);
        }
        Presentation newPresentation = new Presentation(null, newPresentationCode, newPresentationTitle);
        newPresentation.setDescription(newPresentationDescription);
        newPresentation.setSectionList(new ArrayList<Section>());
        newPresentation.setQuestionList(new ArrayList<Question>());
        newPresentation.setUserListAdmin(new ArrayList<User>());
        newPresentation.setUserList(new ArrayList<User>());
        newPresentation.getUserListAdmin().add(userProfile);
        newPresentation = presentationFacade.create(newPresentation);
        newPresentation.setPresentationHaveFeelingList(new ArrayList<PresentationHaveFeeling>());
        for (Feeling feeling : feelingFacade.findDefaultFeelings()) {
            PresentationHaveFeeling f = new PresentationHaveFeeling(newPresentation.getPresentationId(), feeling.getFeelingId());
            f.setOrder(feeling.getFeelingId());
            f.setIsdefault((short) (f.getOrder() == 3 ? 1 : 0));
            f.setFeeling(feeling);
            newPresentation.getPresentationHaveFeelingList().add(f);
        }
        userProfile.getPresentationListAdmin().add(newPresentation);
        newPresentation = presentationFacade.edit(newPresentation);
        loggedSession.setAdminPresentation(presentationFacade.find(newPresentation.getPresentationId()));

        newPresentationCode = newPresentationTitle = newPresentationDescription = "";

        return "adminpresentation";
    }

    public String goToPresentation(Presentation p) {
        loggedSession.setAdminPresentation(p);
        return "adminpresentation";
    }
    
    public String viewPresentation(Presentation p) {
        loggedSession.setViewPresentation(p);
        return "viewpresentation";
    }

    public String savePresentation() {
        loggedSession.setAdminPresentation(presentationFacade.edit(loggedSession.getAdminPresentation()));
        return "adminpresentation";
    }

    private String generateRandomCode() {
        return util.Utilities.getMD5("" + r.nextInt()).substring(0, 6);
    }
}
