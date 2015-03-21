package jkluu1.washington.edu.quizdroid5;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SignalStrength;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by JenniferLuu on 3/5/15.
 */
public class Preferences extends ActionBarActivity {

    private String url_text;
    private int mins_num;
    private boolean downloading;
    private boolean start;


    private PendingIntent pendingIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        downloading = false;
        start = false;

        boolean connected = false;

        final ConnectivityManager conMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            connected = true;
        }

        if (isAirplaneModeOn(getApplicationContext())) {
            Button btn = (Button) findViewById(R.id.btn_set);
            btn.setEnabled(false);

            new AlertDialog.Builder(this)
                    .setTitle("No Connection")
                    .setMessage("Airplane mode is currently on. Would you like to disable it?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            openAirplaneSettings();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else if(!connected) {
            new AlertDialog.Builder(this)
                    .setTitle("No Connection")
                    .setMessage("Please check connectivity")
                    .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else if(!downloading) {
            EditText url = (EditText) findViewById(R.id.input_url);
            url.setEnabled(true);
            EditText mins = (EditText) findViewById(R.id.input_mins);
            mins.setEnabled(true);
            Button btn = (Button) findViewById(R.id.btn_set);
            btn.setEnabled(true);
        }
    }

    public void set(View v) {

        EditText url = (EditText) findViewById(R.id.input_url);
        url_text = url.getText().toString();

        if (!start) {
            if (!url_text.matches("")) { // if url and num are valid
                EditText mins = (EditText) findViewById(R.id.input_mins);
                String mins_string = mins.getText().toString();
                Log.d("test", url_text);
                if (!(mins_string).matches("") && Integer.parseInt(mins_string) > 0) {
                    mins_num = Integer.parseInt(mins_string);

                    Log.d("test", mins_string);

                    // Send user inputs to the receiver
                    Intent intent = new Intent(Preferences.this, Receiver.class);
                    intent.putExtra("url", url_text);

                    pendingIntent = PendingIntent.getBroadcast(Preferences.this,
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Button btn = (Button) findViewById(R.id.btn_set);
                    btn.setText("Stop");
                    start = true;
                    Log.d("test", "before method call");
                    start();
                }
            }
        } else {
            Button btn = (Button) findViewById(R.id.btn_set);
            btn.setText("Set");
            start = false;
            stop();
        }

    }

    // Sets an alarm for the given amount of minutes
    public void start() {
        Log.d("test", "start begin");
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int interval = 1000 * 60 * mins_num;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                interval, pendingIntent);
    }

    // Stops the alarm
    public void stop() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }

    private static boolean isAirplaneModeOn(Context context) {

        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;

    }

    private void openAirplaneSettings() {
        Intent intent = new Intent (Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}

