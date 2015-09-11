package jsf;

import db.Question;
import jsf.util.JsfUtil;
import jsf.util.JsfUtil.PersistAction;
import beans.QuestionFacade;

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

@ManagedBean(name = "questionController")
@SessionScoped
public class QuestionController implements Serializable {

    @EJB
    private beans.QuestionFacade ejbFacade;
    private List<Question> items = null;
    private Question selected;

    public QuestionController() {
    }

    public Question getSelected() {
        return selected;
    }

    public void setSelected(Question selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getQuestionPK().setPresentationId(selected.getPresentation().getPresentationId());
    }

    protected void initializeEmbeddableKey() {
        selected.setQuestionPK(new db.QuestionPK());
    }

    private QuestionFacade getFacade() {
        return ejbFacade;
    }

    public Question prepareCreate() {
        selected = new Question();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("QuestionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("QuestionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("QuestionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Question> getItems() {
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

    public List<Question> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Question> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Question.class)
    public static class QuestionControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            QuestionController controller = (QuestionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "questionController");
            return controller.getFacade().find(getKey(value));
        }

        db.QuestionPK getKey(String value) {
            db.QuestionPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new db.QuestionPK();
            key.setPresentationId(Integer.parseInt(values[0]));
            key.setQuestionId(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(db.QuestionPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getPresentationId());
            sb.append(SEPARATOR);
            sb.append(value.getQuestionId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Question) {
                Question o = (Question) object;
                return getStringKey(o.getQuestionPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Question.class.getName()});
                return null;
            }
        }

    }

}
