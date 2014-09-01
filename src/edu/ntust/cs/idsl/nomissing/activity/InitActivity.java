package edu.ntust.cs.idsl.nomissing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;

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
        if (!app.isInitialized())
            app.setInitialized();
    }

    private void checkRegistered() {
        if (!app.isRegistered())
            app.register();
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
