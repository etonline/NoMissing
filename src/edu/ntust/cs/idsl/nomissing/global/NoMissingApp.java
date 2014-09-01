package edu.ntust.cs.idsl.nomissing.global;

import java.util.UUID;

import android.app.Application;
import android.os.Bundle;
import edu.ntust.cs.idsl.nomissing.constant.Requester;
import edu.ntust.cs.idsl.nomissing.preference.SettingsManager;
import edu.ntust.cs.idsl.nomissing.service.RegistrationService;
import edu.ntust.cs.idsl.nomissing.util.Connectivity;

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

    public boolean isInitialized() {
        return settings.isInitialized();
    }

    public void setInitialized() {
        String uuid = UUID.randomUUID().toString();
        settings.setUUID(uuid);
        settings.setInitialized(true);
    }

    public boolean isRegistered() {
        return settings.isRegistered();
    }

    public void register() {
        if (isNetworkAvailable())
            RegistrationService.startService(this, Requester.NOMISSING_APP, new Bundle());
    }
    
    public void setRegistered(String accessToken) {
        settings.setAccessToken(accessToken);
        settings.setRegistered(true);
    }

    public boolean isNetworkAvailable() {
        return Connectivity.isConnected(getApplicationContext());
    }

}
