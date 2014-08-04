package edu.ntust.cs.idsl.nomissing.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.http.NoMissingResultCode;
import edu.ntust.cs.idsl.nomissing.service.GetWeatherDataService;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class LoginActivity extends Activity implements OnClickListener {
	
	private static final String TAG = LoginActivity.class.getSimpleName();
	private NoMissingApp app;
	
	private EditText editTextUsername;
	private EditText editTextPassword;
	private Button buttonLogin; 
	private Button buttonSignup; 
	
	private String username;
	private String password;
	
	private ProgressDialog progressDialog;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);	
		
		app = (NoMissingApp)getApplicationContext();
		
		buttonLogin = (Button)findViewById(R.id.buttonLogin);		
		editTextUsername =(EditText)findViewById(R.id.editTextUsername);	
		editTextPassword =(EditText)findViewById(R.id.editTextPassword);	
		buttonSignup = (Button)findViewById(R.id.buttonSignup);	
		
		buttonLogin.setOnClickListener(this);
		buttonSignup.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonLogin:
			loginTask();
			break;
			
		case R.id.buttonSignup:
			startActivity(new Intent(LoginActivity.this, SignupActivity.class));
			break;
			
		default:
			break;
		}
	}

	private void loginTask() {
		username = editTextUsername.getText().toString();
		password = editTextPassword.getText().toString();
		
        RequestParams params = new RequestParams();
        params.add("username", username);
        params.add("password", password);
        
        NoMissingHttpClient.getInstance(true);
        NoMissingHttpClient.login(params, new JsonHttpResponseHandler() {
            @Override
			public void onStart() {
            	progressDialog = new ProgressDialog(LoginActivity.this);
        		progressDialog.setMessage(LoginActivity.this.getString(R.string.progress_dialog_please_wait));
        		progressDialog.setCancelable(false);
        		progressDialog.show();
			}

			@Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            	Log.v(NoMissingResultCode.TAG, response.toString());
            	
        		if (progressDialog.isShowing()) {
        			progressDialog.dismiss();
        		}
            	
				try {
					int code = response.getInt("code");
					String message = response.getString("message");
					
	    			switch (code) {
	    			case NoMissingResultCode.LOGIN_SUCCESS:
	    				app.session.createLoginSession(username, password);
	    				startService(new Intent(LoginActivity.this, GetWeatherDataService.class));
	        			startActivity(new Intent(LoginActivity.this, MainActivity.class));
	        			LoginActivity.this.finish();	
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
