package edu.ntust.cs.idsl.nomissing.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import edu.ntust.cs.idsl.nomissing.constant.Category;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.SMSMessage;
import edu.ntust.cs.idsl.nomissing.service.tts.TextToSpeechService;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = SMSReceiver.class.getSimpleName();
    private static final String EXTRA_PDUS = "pdus";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle extras = intent.getExtras();
        handleAction(context, extras);
    }
    
    private void handleAction(Context context, Bundle extras) {
        NoMissingApp app = (NoMissingApp) context.getApplicationContext();
        if (!app.getSettings().isSMSReminderEnabled())
            return;
        
        long currentTime = System.currentTimeMillis();
        SMSMessage smsMessage = new SMSMessage();
        
        StringBuilder message = new StringBuilder();
        Object[] pdus = (Object[]) extras.get(EXTRA_PDUS);

        for (int i = 0; i < pdus.length; i++) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String body = sms.getMessageBody().toString();
            String from = sms.getOriginatingAddress();
            message.append(body);
            
            smsMessage.setFrom(from);
            smsMessage.setMessage(message.toString());
        }
        
        smsMessage.setTime(currentTime);
        int id = DaoFactory.getSQLiteDaoFactory().createSMSMessageDao(context).insert(smsMessage);
        String ttsText = "您有新訊息：" + message.toString();
        
        Bundle extrasForTTS = TextToSpeechService.getExtras(Category.SMS, id, ttsText, 
                app.getSettings().getTTSSpeaker(), app.getSettings().getTTSVolume(), app.getSettings().getTTSSpeed(), "wav");
        TextToSpeechService.startService(context, extrasForTTS);
    }

}
