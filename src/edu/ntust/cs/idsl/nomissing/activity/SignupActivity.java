package edu.ntust.cs.idsl.nomissing.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint("NewApi") 
public class SignupActivity extends Activity implements OnClickListener {
	
	private static final String TAG = SignupActivity.class.getSimpleName();
	private NoMissingApp app;
	
	private Button buttonSignup; 
	private EditText editTextUsername;
	private EditText editTextPassword;
	private EditText editTextConfirmPassword;
	private EditText editTextEmail;
	
	private String username;
	private String password;
	private String confirmPassword;
	private String email;
	
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		app = (NoMissingApp)getApplicationContext();
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);	
		
		buttonSignup = (Button)findViewById(R.id.buttonSignup);		
		editTextUsername =(EditText)findViewById(R.id.editTextUsername);	
		editTextPassword =(EditText)findViewById(R.id.editTextPassword);	
		editTextConfirmPassword =(EditText)findViewById(R.id.editTextConfirmPassword);	
		editTextEmail =(EditText)findViewById(R.id.editTextEmail);	

		buttonSignup.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonSignup:
			signupTask();
			break;

		default:
			break;
		}
		
	}	
	
	private void signupTask() {
		username = editTextUsername.getText().toString();
		password = editTextPassword.getText().toString();
		confirmPassword = editTextConfirmPassword.getText().toString();
		email = editTextEmail.getText().toString();
		
        RequestParams params = new RequestParams();
        params.add("username", username);
        params.add("password", password);
        params.add("confirm_password", confirmPassword);
        params.add("email", email);
        
        NoMissingHttpClient.getInstance(true);
        NoMissingHttpClient.signup(params, new JsonHttpResponseHandler() {
            @Override
			public void onStart() {
            	progressDialog = new ProgressDialog(SignupActivity.this);
        		progressDialog.setMessage(SignupActivity.this.getString(R.string.progress_dialog_please_wait));
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
	    			case NoMissingResultCode.SIGNUP_SUCCESS:
	    				ToastMaker.toast(getApplicationContext(), R.string.signup_toast_text_signup_sucess);
	    				SignupActivity.this.finish();	
	    				break;
	    				
	    			case NoMissingResultCode.SIGNUP_FORM_VALIDATION_FAILED:
	    				ToastMaker.toast(getApplicationContext(), message);
	    				break;
	    				
	    			case NoMissingResultCode.SIGNUP_USERNAME_ALREADY_IN_USE:
	    				ToastMaker.toast(getApplicationContext(), message);
	    				break;
	    				
	    			case NoMissingResultCode.SIGNUP_EMAIL_ADDRESS_ALREADY_IN_USE:
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
