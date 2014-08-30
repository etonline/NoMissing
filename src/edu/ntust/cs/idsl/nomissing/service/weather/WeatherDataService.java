package edu.ntust.cs.idsl.nomissing.service.weather;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.http.NoMissingRoute;
import edu.ntust.cs.idsl.nomissing.http.response.WeatherProperty;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.receiver.WeatherServiceReceiver;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class WeatherDataService extends WeatherService {

    private static final String TAG = WeatherDataService.class.getSimpleName();
    private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.GET_WEATHER_DATA";

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION.equals(action)) {
                handleAction(intent.getExtras());
            }
        }
    }

    @Override
    protected void handleAction(final Bundle extras) {
        NoMissingHttpClient.setAsync(false);
        NoMissingHttpClient.get(NoMissingRoute.WEATHER, app.getSettings().getUUID(), app.getSettings().getAccessToken(),
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Log.v(TAG, response.toString());
                        sendBroadcast(WeatherServiceReceiver.getActionReceiveWeatherServiceStart(WeatherDataService.this));
                        saveData(response);

                        if (successor != null)
                            successor.startAction(WeatherDataService.this, extras);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e(TAG, throwable.toString());
                    }
                });
    }

    @Override
    protected void startAction(Context context, Bundle extras) {
        Intent intent = new Intent(context, WeatherDataService.class);
        intent.setAction(ACTION);
        context.startService(intent);
    }

    private void saveData(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);

                int cityid = Integer.parseInt(jsonObject.getString(WeatherProperty.CITY_ID));
                String city = jsonObject.getString(WeatherProperty.NAME);
                String stno = jsonObject.getString(WeatherProperty.STNO);
                String time = jsonObject.getString(WeatherProperty.TIME);
                String memo = jsonObject.getString(WeatherProperty.MEMO);
                String audio = jsonObject.getString(WeatherProperty.AUDIO);
                String createdAt = jsonObject.getString(WeatherProperty.CREATED_AT);
                String updatedAt = jsonObject.getString(WeatherProperty.UPDATED_AT);

                Weather weather = new Weather(cityid, city, stno, time, memo, audio, createdAt, updatedAt);
                DaoFactory.getSQLiteDaoFactory().createWeatherDao(WeatherDataService.this).update(weather);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

}
