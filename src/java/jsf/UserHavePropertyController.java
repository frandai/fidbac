package jsf;

import db.UserHaveProperty;
import jsf.util.JsfUtil;
import jsf.util.JsfUtil.PersistAction;
import beans.UserHavePropertyFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@ManagedBean(name = "userHavePropertyController")
@SessionScoped
public class UserHavePropertyController implements Serializable {

    @EJB
    private beans.UserHavePropertyFacade ejbFacade;
    private List<UserHaveProperty> items = null;
    private UserHaveProperty selected;

    public UserHavePropertyController() {
    }

    public UserHaveProperty getSelected() {
        return selected;
    }

    public void setSelected(UserHaveProperty selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getUserHavePropertyPK().setPropertyId(selected.getProperty().getPropertyId());
        selected.getUserHavePropertyPK().setUserId(selected.getUser().getUserId());
    }

    protected void initializeEmbeddableKey() {
        selected.setUserHavePropertyPK(new db.UserHavePropertyPK());
    }

    private UserHavePropertyFacade getFacade() {
        return ejbFacade;
    }

    public UserHaveProperty prepareCreate() {
        selected = new UserHaveProperty();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UserHavePropertyCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UserHavePropertyUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UserHavePropertyDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<UserHaveProperty> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<UserHaveProperty> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<UserHaveProperty> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = UserHaveProperty.class)
    public static class UserHavePropertyControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserHavePropertyController controller = (UserHavePropertyController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userHavePropertyController");
            return controller.getFacade().find(getKey(value));
        }

        db.UserHavePropertyPK getKey(String value) {
            db.UserHavePropertyPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new db.UserHavePropertyPK();
            key.setUserId(Integer.parseInt(values[0]));
            key.setPropertyId(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(db.UserHavePropertyPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getUserId());
            sb.append(SEPARATOR);
            sb.append(value.getPropertyId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof UserHaveProperty) {
                UserHaveProperty o = (UserHaveProperty) object;
                return getStringKey(o.getUserHavePropertyPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), UserHaveProperty.class.getName()});
                return null;
            }
        }

    }

}
