package edu.ntust.cs.idsl.nomissing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.model.SMSMessage;
import edu.ntust.cs.idsl.nomissing.service.MediaPlayerService;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class SMSActivity extends Activity {

    private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.CHIME";
    private static final String EXTRA_SMSMESSAGE_ID = "edu.ntust.cs.idsl.nomissing.extra.SMSMESSAGE_ID";
    private SMSMessage smsMessage;

    public static void startAction(Context context, int smsMessageID) {
        context.startActivity(getAction(context, smsMessageID));
    }
    
    public static Intent getAction(Context context, int smsMessageID) {
        Intent intent = new Intent(context, SMSActivity.class);
        intent.setAction(ACTION);
        intent.putExtra(EXTRA_SMSMESSAGE_ID, smsMessageID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        int id = getIntent().getIntExtra(EXTRA_SMSMESSAGE_ID, 0);
        smsMessage = DaoFactory.getSQLiteDaoFactory().createSMSMessageDao(this).find(id);
        
        openSMSDialog(smsMessage);
    }

    private void openSMSDialog(final SMSMessage smsMessage) {
        StringBuilder message = new StringBuilder();
        message.append(smsMessage.getMessage() + "\n");
        message.append("\n" + "¨Ó¦Û: " + smsMessage.getFrom());
        
        AlertDialog cityWeatherDialog = new AlertDialog.Builder(this)
                .setTitle(getTitle())
                .setIcon(android.R.drawable.ic_dialog_email)
                .setMessage(message.toString())
                .setNegativeButton(R.string.alert_dialog_close,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();

        cityWeatherDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        MediaPlayerService.startActionPlay(SMSActivity.this, smsMessage.getAudio());
                    }
                });

        cityWeatherDialog
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        MediaPlayerService.startActionStop(SMSActivity.this, smsMessage.getAudio());
                        finish();
                    }
                });

        cityWeatherDialog.show();
    }

}
