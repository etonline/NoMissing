package edu.ntust.cs.idsl.nomissing.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint("NewApi")
public class MediaPlayerService extends Service implements
		MediaPlayer.OnPreparedListener, 
		MediaPlayer.OnErrorListener,
		MediaPlayer.OnCompletionListener,
		AudioManager.OnAudioFocusChangeListener {

	private static final String TAG = MediaPlayerService.class.getSimpleName();
	public static final String ACTION_PLAY = "edu.ntust.cs.idsl.nomissing.action.PLAY";
    public static final String ACTION_PAUSE = "edu.ntust.cs.idsl.nomissing.action.PAUSE";
    public static final String ACTION_STOP = "edu.ntust.cs.idsl.nomissing.action.STOP";
    public static final String EXTRA_AUDIO_URI = "edu.ntust.cs.idsl.nomissing.extra.AUDIO_URI";
    private MediaPlayer mediaPlayer;

	public static void startActionPlay(Context context, String audioURI) {
		Intent intent = new Intent(context, MediaPlayerService.class);
		intent.setAction(MediaPlayerService.ACTION_PLAY);
		intent.putExtra(EXTRA_AUDIO_URI, audioURI);		
		context.startService(intent);		
	}
	
	public static void startActionPause(Context context, String audioURI) {
		Intent intent = new Intent(context, MediaPlayerService.class);
		intent.setAction(MediaPlayerService.ACTION_PAUSE);
		intent.putExtra(EXTRA_AUDIO_URI, audioURI);		
		context.startService(intent);			
	}
	
	public static void startActionStop(Context context, String audioURI) {
		Intent intent = new Intent(context, MediaPlayerService.class);
		intent.setAction(MediaPlayerService.ACTION_STOP);
		intent.putExtra(EXTRA_AUDIO_URI, audioURI);		
		context.startService(intent);			
	}
    
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
    @Override
	public void onCreate() {
		super.onCreate();
		mediaPlayer = new MediaPlayer(); 
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);
	}

    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			Log.v(TAG, intent.getAction());	
			final String action = intent.getAction();
			final String audioURI = intent.getStringExtra(EXTRA_AUDIO_URI);
			
			if (ACTION_PLAY.equals(action)) {		
				Log.i(TAG, "Audio Playing");
				handleActionPlay(audioURI);
			}
			
			if (ACTION_PAUSE.equals(action)) {		
				Log.i(TAG, "Audio Paused");
				handleActionPause();
			}			
			
			if (ACTION_STOP.equals(action)) {	
				Log.i(TAG, "Audio Stoped");
				handleActionStop();
			}			
		}
		return startId;
    }

    @Override
	public void onDestroy() {
		super.onDestroy();
		if (mediaPlayer != null)
			mediaPlayer.release();
		
		Log.i(TAG, "Service killed");
	}

	@Override
    public void onPrepared(MediaPlayer mp) {
    	mp.start();
    }

	@Override
	public void onCompletion(MediaPlayer mp) {
		stopSelf();
	}
	
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		mp.release();
		mediaPlayer = null;
		return false;
	}	

	@Override
	public void onAudioFocusChange(int focusChange) {
		if (mediaPlayer == null)
			return;

		switch (focusChange) {
		case AudioManager.AUDIOFOCUS_GAIN:
			mediaPlayer.setVolume(0.8f, 0.8f);
			mediaPlayer.start();				
			break;
		case AudioManager.AUDIOFOCUS_LOSS:
			stopSelf();		
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
			if (mediaPlayer.isPlaying())
				mediaPlayer.pause();
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
			if (mediaPlayer.isPlaying())
				mediaPlayer.setVolume(0.1f, 0.1f);
			break;
		}
	}
	
	private void handleActionPlay(String audioURI) {
        try {
    		Uri uri = Uri.parse(audioURI);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);            	
			mediaPlayer.setDataSource(this, uri);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync(); 
		} catch (Exception e) {
			Log.e(TAG , e.toString());
		}	 
	}
	
	private void handleActionPause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		} else {
			mediaPlayer.start();
		}				
	}
	
	private void handleActionStop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			this.stopSelf();
		}			
	}

}
