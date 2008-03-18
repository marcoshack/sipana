package org.sipana.server.web.sip.converter;

import static java.lang.Integer.parseInt;

import java.util.Calendar;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.*;

public class MillisConverter implements Converter {

    public Object getAsObject(FacesContext facesContext,
            UIComponent uiComponent, String param) {
        
        try {
            Calendar calendar = Calendar.getInstance();
            String[] date = param.split("/");
            calendar.set(parseInt(date[2]), (parseInt(date[1])-1), parseInt(date[0]));
            Long millis = calendar.getTimeInMillis();
            return millis;
        } catch (Exception exception) {
            throw new ConverterException(exception);
        }
        
    }

    public String getAsString(FacesContext facesContext,
            UIComponent uiComponent, Object obj) {
        try {
            return "";
        } catch (Exception exception) {
            throw new ConverterException(exception);
        }
    }
}
