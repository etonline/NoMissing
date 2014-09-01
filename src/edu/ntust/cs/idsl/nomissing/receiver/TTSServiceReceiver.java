package edu.ntust.cs.idsl.nomissing.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.SMSActivity;
import edu.ntust.cs.idsl.nomissing.constant.Category;
import edu.ntust.cs.idsl.nomissing.model.ProgressStatus;
import edu.ntust.cs.idsl.nomissing.notification.NotificationHandlerFactory;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

public class TTSServiceReceiver extends BroadcastReceiver {

    private static final String TAG = TTSServiceReceiver.class.getSimpleName();
    private static final String ACTION_RECEIVE_TTS_SERVICE_START = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_TTS_SERVICE_START";
    private static final String ACTION_RECEIVE_TTS_SERVICE_UPDATE_PROGRESS = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_TTS_SERVICE_UPDATE_PROGRESS";
    private static final String ACTION_RECEIVE_TTS_SERVICE_FINISH = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_TTS_SERVICE_FINISH";
    private static final String ACTION_RECEIVE_TTS_SERVICE_FAILURE = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_TTS_SERVICE_FAILURE";
    private static final String EXTRA_CATEGORY = "edu.ntust.cs.idsl.nomissing.extra.CATEGORY";
    private static final String EXTRA_ENTITY_ID = "edu.ntust.cs.idsl.nomissing.extra.ENTITY_ID";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Log.v(TAG, intent.getAction());
            final String action = intent.getAction();

            if (ACTION_RECEIVE_TTS_SERVICE_START.equals(action)) {
                handleActionReceiveTTSServiceStart(context);
                return;
            }

            if (ACTION_RECEIVE_TTS_SERVICE_UPDATE_PROGRESS.equals(action)) {
                handleActionReceiveTTSServiceUpdateProgress(context);
                return;
            }

            if (ACTION_RECEIVE_TTS_SERVICE_FINISH.equals(action)) {
                final Category category = (Category) intent.getSerializableExtra(EXTRA_CATEGORY);
                final long entityID = intent.getLongExtra(EXTRA_ENTITY_ID, 0);
                handleActionReceiveTTSServiceFinish(context, category, entityID);
                return;
            }
            
            if (ACTION_RECEIVE_TTS_SERVICE_FAILURE.equals(action)) {
                handleActionReceiveTTSServiceFailure(context);
                return;
            }
        }
    }
    
    protected void handleActionReceiveTTSServiceStart(Context context) {   
        ProgressStatus progressStatus = new ProgressStatus(
                ProgressStatus.START,
                context.getString(R.string.app_name),
                context.getString(R.string.create_audio_file_start), 0);
        NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);     
    }

    protected void handleActionReceiveTTSServiceUpdateProgress(Context context) {
        ProgressStatus progressStatus = new ProgressStatus(
                ProgressStatus.PROGRESS_UPDATE,
                context.getString(R.string.app_name),
                context.getString(R.string.create_audio_file_start), 50);
        NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);       
    }
    
    protected void handleActionReceiveTTSServiceFinish(Context context, Category category, long entityID) {
        ProgressStatus progressStatus = new ProgressStatus(
                ProgressStatus.FINISH,
                context.getString(R.string.app_name),
                context.getString(R.string.create_audio_file_finish), 100);
        NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);
        ToastMaker.toast(context, R.string.create_audio_file_finish);
        
        if (Category.SMS.equals(category)) {
            SMSActivity.startAction(context, (int) entityID);
        }
    }
    
    protected void handleActionReceiveTTSServiceFailure(Context context) {
        ProgressStatus progressStatus = new ProgressStatus(
                ProgressStatus.FAILURE,
                context.getString(R.string.app_name),
                context.getString(R.string.create_audio_file_failure), 0);
        NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);
        ToastMaker.toast(context, R.string.create_audio_file_failure);        
    }
    
    public static Intent getActionReceiveTTSServiceStart(Context context) {
        Intent intent = new Intent(context, TTSServiceReceiver.class);
        intent.setAction(ACTION_RECEIVE_TTS_SERVICE_START);
        return intent;
    }
    
    public static Intent getActionReceiveTTSServiceUpdateProgress(Context context) {
        Intent intent = new Intent(context, TTSServiceReceiver.class);
        intent.setAction(ACTION_RECEIVE_TTS_SERVICE_UPDATE_PROGRESS);
        return intent;
    }
    
    public static Intent getActionReceiveTTSServiceFinish(Context context, Category category, long entityID) {
        Intent intent = new Intent(context, TTSServiceReceiver.class);
        intent.setAction(ACTION_RECEIVE_TTS_SERVICE_FINISH);
        intent.putExtra(EXTRA_CATEGORY, category);
        intent.putExtra(EXTRA_ENTITY_ID, entityID);
        return intent;
    }
    
    public static Intent getActionReceiveTTSServiceFailure(Context context) {
        Intent intent = new Intent(context, TTSServiceReceiver.class);
        intent.setAction(ACTION_RECEIVE_TTS_SERVICE_FAILURE);
        return intent;
    }
}
