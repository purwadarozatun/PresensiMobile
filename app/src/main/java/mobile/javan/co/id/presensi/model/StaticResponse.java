package mobile.javan.co.id.presensi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Purwa on 21/03/2015.
 */
public class StaticResponse {
    public String responseStatus;
    public Boolean status;


    public Date getDateFrom(String pattern , String input , Date devault){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.parse(input);

        }catch (Exception ex){
            ex.printStackTrace();
            return devault;
        }

    }
    public String getStringFrom(String pattern , Date input , String devault){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(input);

        }catch (Exception ex){
            ex.printStackTrace();
            return devault;
        }

    }
}
