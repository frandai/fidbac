package controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import db.User;
import javax.ejb.EJB;

@ManagedBean(name = "profile")
@ViewScoped
public class Profile implements Serializable {
    
    @ManagedProperty(value="#{loggedSession}")
    private LoggedSession loggedSession;
    @EJB
    private beans.UserFacade userFacade;
    private User userProfile;
    private String usernamePhantom;
    private String userPassword;
    private String userPassword2;

    public LoggedSession getLoggedSession() {
        return loggedSession;
    }

    public void setLoggedSession(LoggedSession loggedSession) {
        this.loggedSession = loggedSession;
    }

    public User getUserProfile() {
        boolean setPhandom = userProfile == null;
        userProfile = getLoggedSession().getLogUser();
        if(userProfile == null) {
            util.Utilities.redirect("index.xhtml");
        } else {
            if(setPhandom) {
                usernamePhantom = userProfile.getUsername();
            }
        }
        return userProfile;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPassword2() {
        return userPassword2;
    }

    public void setUserPassword2(String userPassword2) {
        this.userPassword2 = userPassword2;
    }
    
    public void saveUser() {
        if(!usernamePhantom.equals(userProfile.getUsername()) && userFacade.exists(userProfile.getUsername())) {
            util.Utilities.showMessage("Username already exists. Choose another username.", true);
            return;
        }
        
        if(!userPassword.equals("")) {
            if (!this.userPassword.equals(this.userPassword2)) {
                util.Utilities.showMessage("Passwords don't match.", true);
                return;
            }
            userProfile.setPassword(util.Utilities.getMD5(userPassword));
        }
        
        userProfile = userFacade.edit(userProfile);
        getLoggedSession().setLogUser(userProfile);
        usernamePhantom = userProfile.getUsername();
        util.Utilities.showMessage("User saved succesfully.", false);
    }
}
