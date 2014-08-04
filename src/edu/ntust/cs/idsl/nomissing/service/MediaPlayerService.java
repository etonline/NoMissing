package edu.ntust.cs.idsl.nomissing.service;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.MainActivity;

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
        
    private MediaPlayer mediaPlayer;
    private String audioURL;

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

	public int onStartCommand(Intent intent, int flags, int startId) {
    	
		String action = intent.getAction();
		switch (action) {
		case ACTION_PLAY:
            try {
            	Log.i(TAG, "Audio Playing");
        		audioURL = intent.getStringExtra("audioURL");

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);            	
				mediaPlayer.setDataSource(audioURL);
	            mediaPlayer.setOnPreparedListener(this);
	            mediaPlayer.prepareAsync(); 
			} catch (Exception e) {
				Log.e("MediaPlayerService", e.toString());
			}	 
            break;
            
		case ACTION_PAUSE:	
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				Log.i(TAG, "Audio Paused");
			} else {
				mediaPlayer.start();
			}		
			break;

		case ACTION_STOP:
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				Log.i(TAG, "Audio Stoped");
				this.stopSelf();
			}		
			break;
			
		default:
			break;
		}

			
        
		return startId;
    }

    @Override
	public void onDestroy() {
		super.onDestroy();
		
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}

		Log.i("MediaPlayerService","Service killed");
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
		Toast.makeText(MediaPlayerService.this, "�o�Ϳ��~�A�����", Toast.LENGTH_SHORT).show();
		return false;
	}	

	@Override
	public void onAudioFocusChange(int focusChange) {
		if (mediaPlayer == null)
			return;

		switch (focusChange) {
		case AudioManager.AUDIOFOCUS_GAIN:
			// �{�����o�n�������v
			mediaPlayer.setVolume(0.8f, 0.8f);
			mediaPlayer.start();				
			break;
		case AudioManager.AUDIOFOCUS_LOSS:
			// �{���|���n�������v�A�ӥB�ɶ��i��ܤ[
			stopSelf();		// �����o��Service
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
			// �{���|���n�������v�A���w���ܧִN�|�A���o
			if (mediaPlayer.isPlaying())
				mediaPlayer.pause();
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
			// �{���|���n�������v�A���O�i�H�Ϋܤp�����q�~�򼽩�
			if (mediaPlayer.isPlaying())
				mediaPlayer.setVolume(0.1f, 0.1f);
			break;
		}
		
	}

}
