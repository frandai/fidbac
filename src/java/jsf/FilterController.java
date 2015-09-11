package jsf;

import db.Filter;
import jsf.util.JsfUtil;
import jsf.util.JsfUtil.PersistAction;
import beans.FilterFacade;

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

@ManagedBean(name = "filterController")
@SessionScoped
public class FilterController implements Serializable {

    @EJB
    private beans.FilterFacade ejbFacade;
    private List<Filter> items = null;
    private Filter selected;

    public FilterController() {
    }

    public Filter getSelected() {
        return selected;
    }

    public void setSelected(Filter selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getFilterPK().setPresentationId(selected.getPresentation().getPresentationId());
        selected.getFilterPK().setFilterId(selected.getFilterPK().getFilterId());
    }

    protected void initializeEmbeddableKey() {
        selected.setFilterPK(new db.FilterPK());
    }

    private FilterFacade getFacade() {
        return ejbFacade;
    }

    public Filter prepareCreate() {
        selected = new Filter();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FilterCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FilterUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FilterDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Filter> getItems() {
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

    public List<Filter> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Filter> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Filter.class)
    public static class FilterControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FilterController controller = (FilterController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "filterController");
            return controller.getFacade().find(getKey(value));
        }

        db.FilterPK getKey(String value) {
            db.FilterPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new db.FilterPK();
            key.setPresentationId(Integer.parseInt(values[0]));
            key.setFilterId(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(db.FilterPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getPresentationId());
            sb.append(SEPARATOR);
            sb.append(value.getFilterId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Filter) {
                Filter o = (Filter) object;
                return getStringKey(o.getFilterPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Filter.class.getName()});
                return null;
            }
        }

    }

}
