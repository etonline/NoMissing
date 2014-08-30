package edu.ntust.cs.idsl.nomissing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.service.MediaPlayerService;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class WeatherActivity extends Activity {

    private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.WEATHER";
    private static final String EXTRA_CITYID = "edu.ntust.cs.idsl.nomissing.extra.CITYID";
    private NoMissingApp app;
    private Weather weather;

    public static void startActivity(Context context, int cityID) {
        context.startActivity(getAction(context, cityID));
    }

    public static Intent getAction(Context context, int cityID) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.setAction(ACTION);
        intent.putExtra(EXTRA_CITYID, cityID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        app = (NoMissingApp) getApplicationContext();

        int cityID = getIntent().getIntExtra(EXTRA_CITYID, 0);
        weather = DaoFactory.getSQLiteDaoFactory().createWeatherDao(this).find(cityID);
        openCityWeatherDialog(weather);
    }

    private void openCityWeatherDialog(final Weather weather) {
        AlertDialog cityWeatherDialog = new AlertDialog.Builder(this)
                .setTitle(weather.getCity())
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage(weather.getMemo())
                .setNegativeButton(R.string.alert_dialog_close,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();

        cityWeatherDialog
                .setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        if (app.getSettings().isWeatherTTSEnabled())
                            MediaPlayerService.startActionPlay(WeatherActivity.this, weather.getAudio());
                    }
                });

        cityWeatherDialog
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (app.getSettings().isWeatherTTSEnabled())
                            MediaPlayerService.startActionStop(WeatherActivity.this, weather.getAudio());
                        finish();
                    }
                });

        cityWeatherDialog.show();
    }

}
