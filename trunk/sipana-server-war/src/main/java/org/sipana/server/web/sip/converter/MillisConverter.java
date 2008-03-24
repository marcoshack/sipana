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
            if (param != "") {
                Calendar calendar = Calendar.getInstance();
                String[] date = param.split("/");
                calendar.set(parseInt(date[2]),
                        (parseInt(date[1]) - 1),
                        parseInt(date[0]));
                Long millis = calendar.getTimeInMillis();
                return millis;
            } else
                return 0;
        } catch (Exception exception) {
            throw new ConverterException(exception);
        }

    }

    public String getAsString(FacesContext facesContext,
            UIComponent uiComponent, Object obj) {
        try {
            Long millis = (Long) obj;
            if (millis != 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(millis);
                return "" + calendar.get(Calendar.DAY_OF_MONTH) + "/"
                        + (calendar.get(Calendar.MONTH)+1) + "/"
                        + calendar.get(Calendar.YEAR);
            }
            return "";
        } catch (Exception exception) {
            throw new ConverterException(exception);
        }
    }
}
