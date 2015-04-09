package mobile.javan.co.id.presensi.model;

/**
 * Created by Purwa on 07/04/2015.
 */
public class Settings {

    private String watchNik = "";
    private Boolean isDatangNotifShow = false;
    private Boolean isPulangNotifShow = false;


    public Boolean getIsDatangNotifShow() {
        return isDatangNotifShow;
    }

    public void setIsDatangNotifShow(Boolean isDatangNotifShow) {
        this.isDatangNotifShow = isDatangNotifShow;
    }

    public Boolean getIsPulangNotifShow() {
        return isPulangNotifShow;
    }

    public void setIsPulangNotifShow(Boolean isPulangNotifShow) {
        this.isPulangNotifShow = isPulangNotifShow;
    }

    public String getWatchNik() {
        return watchNik;
    }

    public void setWatchNik(String watchNik) {
        this.watchNik = watchNik;
    }

}
