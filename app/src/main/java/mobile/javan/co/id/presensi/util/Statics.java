package mobile.javan.co.id.presensi.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Purwa on 21/03/2015.
 */
public class Statics {
    public String getStringFromDate(Date input , String pattern , String defaultValue){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(input);

        }catch (Exception ex){
            return null;
        }


    }
    public Date getDateFromString(String input, String pattern , Date defaultValue){
        try {
            DateFormat format = new SimpleDateFormat(pattern);
            Date date = format.parse(input);
            return date;
        }catch (Exception ex){
            return defaultValue;
        }
    }
}
