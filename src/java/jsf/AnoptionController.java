package jsf;

import db.Anoption;
import jsf.util.JsfUtil;
import jsf.util.JsfUtil.PersistAction;
import beans.AnoptionFacade;

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

@ManagedBean(name = "anoptionController")
@SessionScoped
public class AnoptionController implements Serializable {

    @EJB
    private beans.AnoptionFacade ejbFacade;
    private List<Anoption> items = null;
    private Anoption selected;

    public AnoptionController() {
    }

    public Anoption getSelected() {
        return selected;
    }

    public void setSelected(Anoption selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getAnoptionPK().setPresentationId(selected.getQuestion().getQuestionPK().getQuestionId());
        selected.getAnoptionPK().setQuestionId(selected.getQuestion().getQuestionPK().getPresentationId());
    }

    protected void initializeEmbeddableKey() {
        selected.setAnoptionPK(new db.AnoptionPK());
    }

    private AnoptionFacade getFacade() {
        return ejbFacade;
    }

    public Anoption prepareCreate() {
        selected = new Anoption();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AnoptionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AnoptionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AnoptionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Anoption> getItems() {
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

    public List<Anoption> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Anoption> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Anoption.class)
    public static class AnoptionControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AnoptionController controller = (AnoptionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "anoptionController");
            return controller.getFacade().find(getKey(value));
        }

        db.AnoptionPK getKey(String value) {
            db.AnoptionPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new db.AnoptionPK();
            key.setPresentationId(Integer.parseInt(values[0]));
            key.setQuestionId(Integer.parseInt(values[1]));
            key.setOptionId(Integer.parseInt(values[2]));
            return key;
        }

        String getStringKey(db.AnoptionPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getPresentationId());
            sb.append(SEPARATOR);
            sb.append(value.getQuestionId());
            sb.append(SEPARATOR);
            sb.append(value.getOptionId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Anoption) {
                Anoption o = (Anoption) object;
                return getStringKey(o.getAnoptionPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Anoption.class.getName()});
                return null;
            }
        }

    }

}
