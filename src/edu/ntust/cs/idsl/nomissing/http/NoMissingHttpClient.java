package edu.ntust.cs.idsl.nomissing.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class NoMissingHttpClient {

    public static final String TAG = NoMissingHttpClient.class.getSimpleName();
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private static AsyncHttpClient syncHttpClient = new SyncHttpClient();
    private static AsyncHttpClient client;

    public static AsyncHttpClient setAsync(boolean isAsync) {
        client = isAsync ? asyncHttpClient : syncHttpClient;
        return client;
    }

    public static void register(RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(NoMissingRoute.REGISTER, params, responseHandler);
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

    public static void download(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(url, responseHandler);
    }

}
