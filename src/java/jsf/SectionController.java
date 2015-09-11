package jsf;

import db.Section;
import jsf.util.JsfUtil;
import jsf.util.JsfUtil.PersistAction;
import beans.SectionFacade;

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

@ManagedBean(name = "sectionController")
@SessionScoped
public class SectionController implements Serializable {

    @EJB
    private beans.SectionFacade ejbFacade;
    private List<Section> items = null;
    private Section selected;

    public SectionController() {
    }

    public Section getSelected() {
        return selected;
    }

    public void setSelected(Section selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getSectionPK().setPresentationId(selected.getPresentation().getPresentationId());
    }

    protected void initializeEmbeddableKey() {
        selected.setSectionPK(new db.SectionPK());
    }

    private SectionFacade getFacade() {
        return ejbFacade;
    }

    public Section prepareCreate() {
        selected = new Section();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SectionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SectionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SectionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Section> getItems() {
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

    public List<Section> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Section> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Section.class)
    public static class SectionControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SectionController controller = (SectionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "sectionController");
            return controller.getFacade().find(getKey(value));
        }

        db.SectionPK getKey(String value) {
            db.SectionPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new db.SectionPK();
            key.setPresentationId(Integer.parseInt(values[0]));
            key.setSectionId(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(db.SectionPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getPresentationId());
            sb.append(SEPARATOR);
            sb.append(value.getSectionId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Section) {
                Section o = (Section) object;
                return getStringKey(o.getSectionPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Section.class.getName()});
                return null;
            }
        }

    }

}
