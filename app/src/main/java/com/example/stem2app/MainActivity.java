package com.example.stem2app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends AppCompatActivity {

    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    private TextView spokenText;
    TextView dateScript;
    Button begin;
    ImageView iv;

    BitmapDrawable drawable;
    Bitmap bitmap;

    String imageString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        begin=(Button)findViewById(R.id.beginBtn);
        iv=(ImageView)findViewById(R.id.image_view);


        //Run Python Script
        dateScript = findViewById(R.id.dateScript);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        //create python instance
        final Python py =Python.getInstance();

        begin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawable=(BitmapDrawable)iv.getDrawable();
                bitmap=drawable.getBitmap();
                imageString=getStringImage(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                //create python object
                PyObject pyObj = py.getModule("sample");

                //call function, pass imageString as a parameter into the script
                PyObject obj = pyObj.callAttr("isColorChange",byteArray);

                //set returned text to textView
                dateScript.setText(obj.toString());
            }
        });

    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos); //change quality?

        //store in byte array
        byte[] imageBytes = baos.toByteArray();
        //encode to string
        String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }




}