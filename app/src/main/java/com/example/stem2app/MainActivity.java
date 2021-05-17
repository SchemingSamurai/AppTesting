package com.example.stem2app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends AppCompatActivity {

    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    private TextView spokenText;
    TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Run Python Script
        dateText = findViewById(R.id.dateScript);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        //create python instance
        Python py =Python.getInstance();
        //create python object
        PyObject pyObj = py.getModule("sample");

        //call function
        PyObject obj = pyObj.callAttr("main");

        //set returned text to textView
        dateText.setText(obj.toString());



        //Voice Commands
        //ask device for permissions
        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);

        spokenText = findViewById(R.id.saidTxt);
        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String string = "";
                if (matches != null) {
                    string = matches.get(0);
                    spokenText.setText(string);
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }


    public void BeginButton(View view){
                speechRecognizer.startListening(intentRecognizer);
                Toast toast = Toast.makeText(getApplicationContext(), "Clicked Begin", Toast.LENGTH_LONG);
                toast.show();
            }



    public void StopButton(View view){
                speechRecognizer.stopListening();
                Toast toast = Toast.makeText(getApplicationContext(), "Clicked Stop", Toast.LENGTH_LONG);
                toast.show();
            }



}