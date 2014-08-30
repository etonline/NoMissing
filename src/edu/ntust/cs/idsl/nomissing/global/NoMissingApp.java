package edu.ntust.cs.idsl.nomissing.global;

import android.app.Application;
import edu.ntust.cs.idsl.nomissing.preference.SettingsManager;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class NoMissingApp extends Application {

    private SettingsManager settings;

    @Override
    public void onCreate() {
        super.onCreate();
        settings = SettingsManager.getInstance(getApplicationContext());
    }

    public SettingsManager getSettings() {
        return settings;
    }

}
