package jkluu1.washington.edu.quizdroid5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JenniferLuu on 2/27/15.
 */

public class Receiver extends BroadcastReceiver {

    private static String url;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("test", "received");

        this.context = context;

        url = intent.getStringExtra("url");
        new GetTopics().execute();
    }

    private class GetTopics extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", jsonStr);

            if (jsonStr != null) {

                String filename = "quizdata.json";

                FileOutputStream outputStream;

                try {
                    outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(jsonStr.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Unable to retrieve data from the URL");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}
