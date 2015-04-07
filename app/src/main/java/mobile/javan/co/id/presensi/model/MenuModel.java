package mobile.javan.co.id.presensi.model;

/**
 * Created by Purwa on 06/04/2015.
 */
public class MenuModel {
    private String menuLabel;
    private String menuIcon;

    public MenuModel(String menuIcon, String menuLabel) {
        this.menuIcon = menuIcon;
        this.menuLabel = menuLabel;
    }

    public String getMenuLabel() {
        return menuLabel;
    }

    public void setMenuLabel(String menuLabel) {
        this.menuLabel = menuLabel;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }
}
