package edu.ntust.cs.idsl.nomissing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import edu.ntust.cs.idsl.nomissing.R;

public class AboutActivity extends Activity {
    
    private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.ABOUT";
    
    public static void startAction(Context context) {
        context.startActivity(getAction(context));
    }
    
    public static Intent getAction(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        intent.setAction(ACTION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        openAboutDialog();
    }
    
    private void openAboutDialog() {
        AlertDialog aboutDialog = new AlertDialog.Builder(this)
                .setTitle(getTitle())
                .setIcon(R.drawable.ic_launcher)
                .setMessage(R.string.nomissing_about)
                .setNegativeButton(R.string.alert_dialog_close,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();
        
        aboutDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

        aboutDialog.show();
    }

}
