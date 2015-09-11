package controller;

import db.Presentation;
import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import db.User;
import db.UserHaveProperty;
import java.util.Calendar;
import java.util.Random;
import javax.faces.bean.ManagedProperty;

@ManagedBean(name = "loggedSession")
@SessionScoped
public class LoggedSession implements Serializable {

    @ManagedProperty(value = "#{client}")
    private Client client;

    @EJB
    private beans.UserFacade userFacade;
    @EJB
    private beans.PropertyFacade propertyFacade;

    private User logUser = null;
    private String loginUser;
    private String loginPassword;
    private String signupUser;
    private String signupPassword;
    private String signupPassword2;
    private Presentation adminPresentation;
    private Presentation viewPresentation;
    private User backupVirtualUser;
    Random r = new Random();

    public Presentation getViewPresentation() {
        return viewPresentation;
    }

    public void setViewPresentation(Presentation viewPresentation) {
        this.viewPresentation = viewPresentation;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getErrorMessage() {
        String eMessage = client.getErrorMessage();
        return eMessage;
    }

    public Presentation getAdminPresentation() {
        return adminPresentation;
    }

    public void setAdminPresentation(Presentation adminPresentation) {
        this.adminPresentation = adminPresentation;
    }

    public User getBackupVirtualUser() {
        if (backupVirtualUser == null) {
            backupVirtualUser = createVirtualUser();
        }
        return backupVirtualUser;
    }

    private User createVirtualUser() {
        User newVirtualUser = new User(-(Math.abs(r.nextInt() + getCurrentMilis())));
        newVirtualUser.setUserHavePropertyList(new ArrayList<UserHaveProperty>());
        return newVirtualUser;
    }

    private int getCurrentMilis() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long howMany = (c.getTimeInMillis() - System.currentTimeMillis());
        return (int) howMany;
    }

    public boolean isLogged() {
        if (client.getVirtualUser() == null) {
            client.setVirtualUser((logUser != null) ? this.getLogUser() : getBackupVirtualUser());
        }
        return logUser != null;
    }

    public User getLogUser() {
        return logUser;
    }

    public void refreshLogUser() {
        logUser = userFacade.find(logUser.getUsername(), logUser.getPassword());
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getSignupUser() {
        return signupUser;
    }

    public void setSignupUser(String signupUser) {
        this.signupUser = signupUser;
    }

    public String getSignupPassword() {
        return signupPassword;
    }

    public void setSignupPassword(String signupPassword) {
        this.signupPassword = signupPassword;
    }

    public String getSignupPassword2() {
        return signupPassword2;
    }

    public void setSignupPassword2(String signupPassword2) {
        this.signupPassword2 = signupPassword2;
    }

    public void setLogUser(User logUser) {
        this.logUser = logUser;
    }

    public void login() {
        logUser = userFacade.find(this.getLoginUser().trim(), util.Utilities.getMD5(this.getLoginPassword()));
        if (!this.isLogged()) {
            util.Utilities.showMessage("Incorrect username or password.", true);
            return;
        }
        client.setVirtualUser(logUser);
        util.Utilities.showMessage("Logged in succesfully.", false);
    }

    public String logout() {
        logUser = null;
        client.exit();
        client.setVirtualUser(backupVirtualUser);
        client.updatePresentationFeelings();
        return "index";
    }

    public void signup() {
        this.setSignupUser(this.getSignupUser().toLowerCase().trim());

        if (!this.getSignupPassword().equals(this.getSignupPassword2())) {
            util.Utilities.showMessage("Passwords don't match.", true);
            return;
        }
        if (userFacade.exists(this.getSignupUser())) {
            util.Utilities.showMessage("The username already exists.", true);
            return;
        }
        User createUser = userFacade.create(new User(null, this.getSignupUser(), util.Utilities.getMD5(this.getSignupPassword())));
        ArrayList<UserHaveProperty> defaultPropertires = propertyFacade.getDefaultPropertires(createUser.getUserId());
        createUser.setUserHavePropertyList(defaultPropertires);
        logUser = userFacade.edit(createUser);
        refreshLogUser();
        util.Utilities.showMessage("User created succesfully.", false);
    }
}
