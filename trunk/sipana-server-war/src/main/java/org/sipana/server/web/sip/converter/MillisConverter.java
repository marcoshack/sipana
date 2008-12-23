/**
 * This file is part of Sipana project <http://sipana.org/>
 *
 * Sipana is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * Sipana is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sipana.server.web.sip.converter;

import static java.lang.Integer.parseInt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class MillisConverter implements Converter {

    public Object getAsObject(FacesContext facesContext,
            UIComponent uiComponent, String param) throws ConverterException{

        if (param != "") {
            
            if(param.matches("(\\d{2}/){2}\\d{4}\\s+\\d{2}:\\d{2}\\s*") 
                    || param.matches("(\\d{2}/){2}\\d{4}\\s*")) {
                
                Calendar calendar = Calendar.getInstance();
                String[] date = param.split("/|\\s+|:");
                    
                if(date.length==3){
                    calendar.set(parseInt(date[2]), (parseInt(date[1]) - 1),
                            parseInt(date[0]));
                }else {
                    calendar.set(parseInt(date[2]), (parseInt(date[1]) - 1),
                            parseInt(date[0]),parseInt(date[3]),
                            parseInt(date[4]));
                }
                    
                Long millis = calendar.getTimeInMillis();
                return millis;
                
            } else {
            
                FacesMessage message = new FacesMessage("InvalidDateFormatException",
                                        "*Invalid Date - Try dd/MM/yyyy hh:mm");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ConverterException(message);      
            }
        }
        return 0;
    }

    public String getAsString(FacesContext facesContext,
            UIComponent uiComponent, Object obj) {
        
        String check = obj.toString();
        
        if(check.matches("\\d+") && !check.equals("0")){

            Long millis = (Long) obj;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return df.format(calendar.getTime());
        }
        return "";
    }         

}

