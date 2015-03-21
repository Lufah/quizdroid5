package jkluu1.washington.edu.quizdroid5;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JenniferLuu on 2/21/15.
 */
public class QuizApp extends Application implements TopicRepository{
    private static QuizApp instance;
    private List<Topic> topics;
    private boolean getFromFile;

    private static final String TAG_TITLE = "title";
    private static final String TAG_DESC = "desc";
    private static final String TAG_LONGDESC = "longdesc";
    private static final String TAG_QUESTIONS = "questions";
    private static final String TAG_TEXT = "text";
    private static final String TAG_ANSWER = "answer";
    private static final String TAG_ANSWERS = "answers";

    public QuizApp() {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("You cannot instantiate more than one MyApp");
        }
    }

    public QuizApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("quizdroid3", "QuizApp loaded!");
        getFromFile = true;

        generate();
    }

    // Reads from text file
    public void generate() {

        if (getFromFile == true) {
            InputStream is = getResources().openRawResource(R.raw.quizdata);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int ctr;

            try {
                Log.d("test", "file was read");
                ctr = is.read();
                while (ctr != -1) {
                    byteArrayOutputStream.write(ctr);
                    ctr = is.read();
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Text data", byteArrayOutputStream.toString());
            try {
                Log.d("test", "trying to add");
                topics = new ArrayList<Topic>();

                // JSONObject jsonObject = new JSONObject(byteArrayOutputStream.toString());
                JSONArray all = new JSONArray(byteArrayOutputStream.toString());

                for (int i = 0; i < all.length(); i++) {

                    JSONObject t = all.getJSONObject(i);

                    String title = t.getString(TAG_TITLE);
                    String desc = t.getString(TAG_DESC);
                    String longDesc = "";
                    if (t.has("longdesc")) {
                        longDesc = t.getString(TAG_LONGDESC);
                    } else {
                        longDesc = desc;
                    }

                    JSONArray questions = t.getJSONArray(TAG_QUESTIONS);
                    List<Quiz> quizList = new ArrayList<>();
                    for (int k = 0; k < questions.length(); k++) {

                        JSONObject q = questions.getJSONObject(k);
                        String text = q.getString(TAG_TEXT);
                        String answer = q.getString(TAG_ANSWER);
                        JSONArray answers = q.getJSONArray(TAG_ANSWERS);
                        List<String> allAnswers = new ArrayList<>();
                        for (int j = 0; j < answers.length(); j++) {
                            allAnswers.add(answers.getString(j));
                            Log.d("answers: ", answers.getString(j));
                        }
                        Quiz quiz = createQuiz(text, allAnswers, Integer.parseInt(answer));
                        quizList.add(quiz);
                    }
                    Topic topic = createTopic(title, desc, longDesc, quizList);
                    addTopic(topic);
                }
                Log.d("test", "Title: " + topics.get(0).getTitle());

            } catch (JSONException e) {
                Log.d("test", "failed to add");
                e.printStackTrace();

            }
        }
    }
    public Topic createTopic (String t, String sd, String ld, List<Quiz> aq) {
        return new Topic(t, sd, ld, aq);
    }

    public Quiz createQuiz (String ques, List<String> ans, int correct) {
        return new Quiz(ques, ans, correct);
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
    }

    public List<Topic> getTopics() {
        return topics;
    }
}

