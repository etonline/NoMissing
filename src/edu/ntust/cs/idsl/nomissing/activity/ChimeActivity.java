package edu.ntust.cs.idsl.nomissing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.service.MediaPlayerService;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class ChimeActivity extends Activity {

    private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.CHIME";
    private static final String EXTRA_CHIME_ID = "edu.ntust.cs.idsl.nomissing.extra.CHIME_ID";
    private Chime chime;

    public static void startAction(Context context, int chimeID) {
        context.startActivity(getAction(context, chimeID));
    }
    
    public static Intent getAction(Context context, int chimeID) {
        Intent intent = new Intent(context, ChimeActivity.class);
        intent.setAction(ACTION);
        intent.putExtra(EXTRA_CHIME_ID, chimeID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chime);

        int chimeID = getIntent().getIntExtra(EXTRA_CHIME_ID, 0);
        chime = DaoFactory.getSQLiteDaoFactory().createChimeDao(this).find(chimeID);
        openChimeDialog(chime);

        if (!chime.isRepeating()) {
            chime.setTriggered(true);
            DaoFactory.getSQLiteDaoFactory().createChimeDao(this).update(chime);
        }
    }

    private void openChimeDialog(final Chime chime) {
        AlertDialog cityWeatherDialog = new AlertDialog.Builder(this)
                .setTitle(getTitle())
                .setIcon(R.drawable.ic_action_alarms)
                .setMessage(chime.getTime())
                .setNegativeButton(R.string.alert_dialog_close,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();

        cityWeatherDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        MediaPlayerService.startActionPlay(ChimeActivity.this, chime.getAudio());
                    }
                });

        cityWeatherDialog
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        MediaPlayerService.startActionStop(ChimeActivity.this, chime.getAudio());
                        finish();
                    }
                });

        cityWeatherDialog.show();
    }

}
