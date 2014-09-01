package edu.ntust.cs.idsl.nomissing.service.tts;

import android.app.IntentService;
import android.content.Context;
import android.os.Bundle;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.constant.Category;
import edu.ntust.cs.idsl.nomissing.constant.Requester;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.service.RegistrationService;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public abstract class TextToSpeechService extends IntentService {

    public static final String EXTRA_CATEGORY = "edu.ntust.cs.idsl.nomissing.extra.CATEGORY";
    public static final String EXTRA_ENTITY_ID = "edu.ntust.cs.idsl.nomissing.extra.ENTITY_ID";
    public static final String EXTRA_TTS_TEXT = "edu.ntust.cs.idsl.nomissing.extra.TTS_TEXT";
    public static final String EXTRA_TTS_SPEAKER = "edu.ntust.cs.idsl.nomissing.extra.TTS_SPEAKER";
    public static final String EXTRA_TTS_VOLUME = "edu.ntust.cs.idsl.nomissing.extra.TTS_VOLUME";
    public static final String EXTRA_TTS_SPEED = "edu.ntust.cs.idsl.nomissing.extra.TTS_SPEED";
    public static final String EXTRA_TTS_OUTPUT_TYPE = "edu.ntust.cs.idsl.nomissing.extra.OUTPUT_TYPE ";
    public static final String EXTRA_CONVERT_ID = "edu.ntust.cs.idsl.nomissing.extra.CONVERT_ID";
    public static final String EXTRA_AUDIO_URL = "edu.ntust.cs.idsl.nomissing.extra.AUDIO_URL";

    protected TextToSpeechService successor;
    protected NoMissingApp app;

    public static boolean startService(Context context, Bundle extras) {
        boolean isStart = false;

        NoMissingApp app = (NoMissingApp) context.getApplicationContext();
        
        if (!app.isNetworkAvailable()) {
            ToastMaker.toast(context, R.string.toast_network_inavailable);
        }
        
        else if (!app.isRegistered()) {
            RegistrationService.startService(context, Requester.TTS_SERVICE, extras);
        }
        
        else {
            new TTSConvertTextService().startAction(context, extras);
            isStart = true;
        }
        
        return isStart;
    }

    public TextToSpeechService() {
        super("TextToSpeechService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = (NoMissingApp) getApplicationContext();

        if (getClass().equals(TTSConvertTextService.class))
            successor = new TTSGetConvertStatusService();

        if (getClass().equals(TTSGetConvertStatusService.class))
            successor = new TTSGetAudioService();

        if (getClass().equals(TTSGetAudioService.class))
            successor = null;
    }

    public TextToSpeechService getSuccessor() {
        return successor;
    }

    public void setSuccessor(TextToSpeechService successor) {
        this.successor = successor;
    }

    protected abstract void handleAction(Bundle extras);

    protected abstract void startAction(Context context, Bundle extras);
    
    public static Bundle getExtras(Category category, long entityID,
            String ttsText, String ttsSpeaker, int ttsVolume, int ttsSpeed, String outputType) {
        
        Bundle extras = new Bundle();
        extras.putSerializable(TextToSpeechService.EXTRA_CATEGORY, category);
        extras.putLong(TextToSpeechService.EXTRA_ENTITY_ID, entityID);
        extras.putString(TextToSpeechService.EXTRA_TTS_TEXT,ttsText);
        extras.putString(TextToSpeechService.EXTRA_TTS_SPEAKER, ttsSpeaker);
        extras.putInt(TextToSpeechService.EXTRA_TTS_VOLUME, ttsVolume);
        extras.putInt(TextToSpeechService.EXTRA_TTS_SPEED, ttsSpeed);
        extras.putString(TextToSpeechService.EXTRA_TTS_OUTPUT_TYPE, outputType);
        
        return extras;
    }

}
