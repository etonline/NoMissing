package edu.ntust.cs.idsl.nomissing.activity;

import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.http.NoMissingResultCode;
import edu.ntust.cs.idsl.nomissing.util.Connectivity;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;


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
		
		app = (NoMissingApp)getApplicationContext();
		startApp();
	}
	
	/**
	 * Start the app.
	 * If network is unavailable, open the alert dialog and then exit the app.
	 * If the user isn't logged in, go to the LoginActivity.
	 */		
	private void startApp() {
		if (!Connectivity.isConnected(getApplicationContext()))
			openNetworkUnavailableDialog();

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (app.session.isLoggedIn()) {
					loginTask();
				}
				
				// Go to the LoginActivity if the user isn't logged in.
				else {
					startActivity(new Intent(InitActivity.this, LoginActivity.class));
					InitActivity.this.finish();
				}
			}
		}, 1000);
	}
	
	/**
	 * Open the alert dialog.
	 */	
	private void openNetworkUnavailableDialog() {
		new AlertDialog.Builder(InitActivity.this)
		.setTitle(R.string.init_alert_dialog_title_network_inavailable)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setMessage(R.string.init_alert_dialog_message_network_inavailable)
		.setNegativeButton(R.string.init_alert_dialog_button_exit,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						android.os.Process.killProcess(android.os.Process.myPid());
	                    System.exit(1);					
					}
				}).show();	
	}
	
	/**
	 * An asynchronous task for logging in the user.
	 * If login sucessfully, it will start the GetWeatherDataService and go to the MainActivity.
	 */
	private void loginTask() {
        HashMap<String, String> user = app.session.getUserData();
        RequestParams params = new RequestParams(user);
        
        NoMissingHttpClient.getInstance(true);
        NoMissingHttpClient.login(params, new JsonHttpResponseHandler() {
			@Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            	Log.v(NoMissingResultCode.TAG, response.toString());
            	
				try {
					int code = response.getInt("code");
					String message = response.getString("message");
					
	    			switch (code) {
	    			case NoMissingResultCode.LOGIN_SUCCESS:
//	    				startService(new Intent(InitActivity.this, GetWeatherDataService.class));
	        			startActivity(new Intent(InitActivity.this, MainActivity.class));
	        			InitActivity.this.finish();	
	    				break;
	    				
	    			case NoMissingResultCode.LOGIN_FORM_VALIDATION_FAILED:
	    				ToastMaker.toast(getApplicationContext(), message);
	    				break;
	    				
	    			case NoMissingResultCode.LOGIN_INVALID_LOGIN:
	    				ToastMaker.toast(getApplicationContext(), message);
	    				break;
	    				
	    			default:
	    				ToastMaker.toast(getApplicationContext(), message);
	    				break;
	    			}
				} catch (JSONException e) {
					e.printStackTrace();
				}
            }
        });		
	}
    
}
