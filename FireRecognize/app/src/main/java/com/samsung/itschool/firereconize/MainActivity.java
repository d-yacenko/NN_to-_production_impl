package com.samsung.itschool.firereconize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static final String NETWORK_FILE = "fire_net.pt";
    public static final int CAMERA_REQUEST_CODE = 001;
    public static final int FILE_REQUEST_CODE = 002;
    Button captureBtn, fileBtn;
    Bitmap imageBitmap = null;
    Classifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        classifier = new Classifier(Utils.assetFilePath(this, NETWORK_FILE));
        captureBtn = findViewById(R.id.capture);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            }
        });
        fileBtn = findViewById(R.id.file);
        fileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("*/*");
                startActivityForResult(galleryIntent, FILE_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            imageBitmap = (Bitmap) data.getExtras().get("data");
        }
        if (requestCode == FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData(); // String mimeType = getContentResolver().getType(uri);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                imageBitmap = BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException e) {
                Toast.makeText(MainActivity.this, "Нет файла", Toast.LENGTH_SHORT).show();
            }
        }
        if (imageBitmap != null) {
            String pred = classifier.predict(imageBitmap);
            Toast.makeText(this, pred, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Что то пошло не так", Toast.LENGTH_LONG).show();
        }

    }

}