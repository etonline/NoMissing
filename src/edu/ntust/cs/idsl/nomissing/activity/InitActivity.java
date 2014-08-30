package edu.ntust.cs.idsl.nomissing.activity;

import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.service.RegistrationService;
import edu.ntust.cs.idsl.nomissing.util.Connectivity;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class InitActivity extends Activity {

    private static final String TAG = InitActivity.class.getSimpleName();
    private NoMissingApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        app = (NoMissingApp) getApplicationContext();

        checkInitialized();
        checkRegistered();
        startApp();
    }

    private void checkInitialized() {
        if (app.getSettings().isInitialized())
            return;

        String uuid = UUID.randomUUID().toString();
        app.getSettings().setUUID(uuid);
        app.getSettings().setInitialized(true);
    }

    private void checkRegistered() {
        if (app.getSettings().isRegistered())
            return;
        if (Connectivity.isConnected(getApplicationContext()))
            RegistrationService.startService(this, app.getSettings().getUUID());
    }

    private void startApp() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.startAction(InitActivity.this);
                finish();
            }
        }, 3000);
    }

}
