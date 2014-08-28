package edu.ntust.cs.idsl.nomissing.activity;

import java.util.UUID;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.http.parameter.RegisterParameter;
import edu.ntust.cs.idsl.nomissing.http.response.RegisterProperty;
import edu.ntust.cs.idsl.nomissing.http.resultcode.RegisterResultCode;
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
			registerTask();
	}

	private void startApp() {
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(InitActivity.this, MainActivity.class));
				finish();
			}
		}, 1000);
	}

	public void registerTask() {
		RequestParams params = new RequestParams();
		params.add(RegisterParameter.UUID, app.getSettings().getUUID());

		NoMissingHttpClient.setAsync(true);
		NoMissingHttpClient.register(params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.i(NoMissingHttpClient.TAG, response.toString());

				try {
					int code = response.getInt(RegisterProperty.CODE);
					String message = response.getString(RegisterProperty.MESSAGE);

					switch (code) {
					case RegisterResultCode.SUCCESS:
						String accessToken = response.getString(RegisterProperty.ACCESS_TOKEN);
						app.getSettings().setAccessToken(accessToken);
						app.getSettings().setRegistered(true);
						break;

					case RegisterResultCode.INVALID_REGISTRATION:
						Log.e(TAG, message);
						break;

					case RegisterResultCode.UUID_ALREADY_IN_USE:
						Log.e(TAG, message);
						break;
					}
				} catch (JSONException e) {
					Log.i(NoMissingHttpClient.TAG, e.toString());
				}
			}
		});
	}

}
