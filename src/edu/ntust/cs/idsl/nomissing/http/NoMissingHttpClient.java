package edu.ntust.cs.idsl.nomissing.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class NoMissingHttpClient {

	  private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
	  private static AsyncHttpClient syncHttpClient = new SyncHttpClient();
	  private static AsyncHttpClient client;

	  public static AsyncHttpClient getInstance(boolean isAsync) {		  
		  client = isAsync ? asyncHttpClient : syncHttpClient;
		  return client;
	  }
	  
	  public static void login(RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  client.post(NoMissingRoute.LOGIN, params, responseHandler);
	  }
	  
	  public static void logout(RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  client.get(NoMissingRoute.LOGOUT, params, responseHandler);
	  }	  
	  
	  public static void signup(RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  client.post(NoMissingRoute.SIGNUP, params, responseHandler);
	  }	  
	  
	  public static void get(String url, String username, String password, AsyncHttpResponseHandler responseHandler) {
		  client.setBasicAuth(username, password);
		  client.get(url, responseHandler);
	  }

	  public static void post(String url, String username, String password, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  client.setBasicAuth(username, password);
		  client.post(url, params, responseHandler);
	  }
	  
	  public static void put(String url, String username, String password, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  client.setBasicAuth(username, password);
		  client.put(url, params, responseHandler);
	  }
	  
	  public static void delete(String url, String username, String password, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.setBasicAuth(username, password);
		  client.delete(url, responseHandler);
	  }
	  
	  public static void ttsConvertText(String username, String password, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  client.setBasicAuth(username, password);
		  client.post(NoMissingRoute.CONVERT_TEXT, params, responseHandler);
	  }
	  
	  public static void ttsGetConvertStatus(String username, String password, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  client.setBasicAuth(username, password);
		  client.post(NoMissingRoute.GET_CONVERT_STATUS, params, responseHandler);
	  }
	
}
