package jkluu1.washington.edu.quizdroid5;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    protected QuizApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File file = new File(getApplicationContext().getFilesDir(), "quizdata.json");

        app = (QuizApp) getApplicationContext();


        List<String> names = new ArrayList<String>();
        for (int i = 0; i < app.getTopics().size(); i++) {
            names.add(app.getTopics().get(i).getTitle());
        }

        List<String> allShortDesc = new ArrayList<String>();
        allShortDesc.add(app.getTopics().get(0).getShortDescription());
        allShortDesc.add(app.getTopics().get(1).getShortDescription());
        allShortDesc.add(app.getTopics().get(2).getShortDescription());

        Category category_data[] = new Category[]{
                new Category(R.drawable.ic_launcher, allShortDesc.get(0), names.get(0)),
                new Category(R.drawable.ic_launcher, allShortDesc.get(1), names.get(1)),
                new Category(R.drawable.ic_launcher, allShortDesc.get(2), names.get(2))
        };

        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.listview_row, category_data);

        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Log.d("test", "Position " + position + " pressed");

                Topic topic = app.getTopics().get(position);

                Intent intent = new Intent(MainActivity.this, QuizBody.class);
                intent.putExtra("topic", topic);

                startActivity(intent);

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_preferences:
                Intent intent = new Intent(MainActivity.this, Preferences.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
