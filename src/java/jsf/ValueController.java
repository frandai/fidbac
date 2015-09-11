package jsf;

import db.Value;
import jsf.util.JsfUtil;
import jsf.util.JsfUtil.PersistAction;
import beans.ValueFacade;

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

@ManagedBean(name = "valueController")
@SessionScoped
public class ValueController implements Serializable {

    @EJB
    private beans.ValueFacade ejbFacade;
    private List<Value> items = null;
    private Value selected;

    public ValueController() {
    }

    public Value getSelected() {
        return selected;
    }

    public void setSelected(Value selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getValuePK().setPropertyId(selected.getProperty().getPropertyId());
    }

    protected void initializeEmbeddableKey() {
        selected.setValuePK(new db.ValuePK());
    }

    private ValueFacade getFacade() {
        return ejbFacade;
    }

    public Value prepareCreate() {
        selected = new Value();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ValueCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ValueUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ValueDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Value> getItems() {
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

    public List<Value> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Value> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Value.class)
    public static class ValueControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ValueController controller = (ValueController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "valueController");
            return controller.getFacade().find(getKey(value));
        }

        db.ValuePK getKey(String value) {
            db.ValuePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new db.ValuePK();
            key.setValueId(Integer.parseInt(values[0]));
            key.setPropertyId(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(db.ValuePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getValueId());
            sb.append(SEPARATOR);
            sb.append(value.getPropertyId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Value) {
                Value o = (Value) object;
                return getStringKey(o.getValuePK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Value.class.getName()});
                return null;
            }
        }

    }

}
