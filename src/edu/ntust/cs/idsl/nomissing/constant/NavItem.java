package edu.ntust.cs.idsl.nomissing.constant;

import java.util.ArrayList;
import java.util.List;

import edu.ntust.cs.idsl.nomissing.R;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public enum NavItem {

    HOME(R.drawable.ic_action_view_as_list, R.string.nav_home), 
    CALENDAR(R.drawable.ic_action_go_to_today, R.string.nav_calendar), 
    CHIME(R.drawable.ic_action_alarms, R.string.nav_chime), 
    WEATHER(R.drawable.ic_action_cloud, R.string.nav_weather);
//    SETTING(R.drawable.ic_action_settings, R.string.nav_settings), 
//    LOGOUT(android.R.drawable.ic_lock_power_off, R.string.nav_exit);

    private int icon;
    private int title;
    private int count;
    private boolean isCounterVisible = false;

    private NavItem(int icon, int title) {
        this.icon = icon;
        this.title = title;
    }

    private NavItem(int icon, int title, int count,
            boolean isCounterVisible) {
        this.icon = icon;
        this.title = title;
        this.count = count;
        this.isCounterVisible = isCounterVisible;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isCounterVisible() {
        return isCounterVisible;
    }

    public void setCounterVisible(boolean isCounterVisible) {
        this.isCounterVisible = isCounterVisible;
    }

    public static List<NavItem> getNavItems() {
        List<NavItem> navItems = new ArrayList<NavItem>();
        for (NavItem navItem : NavItem.values()) {
            navItems.add(navItem);
        }
        return navItems;
    }

}
