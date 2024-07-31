package com.example.una_test;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestStreamActivity extends AppCompatActivity {
    private TextView mTxtReadFileResult, mTxtOutputResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_stream);

        //copy resource txt file to output new file
        findViewById(R.id.txt_read).setOnClickListener(v -> {
            String text = readFile(getResources().openRawResource(R.raw.test_file));
            mTxtReadFileResult.setText(text);
            outputFile(text);
        });
        mTxtReadFileResult = findViewById(R.id.txt_read_file_result);

        //show copy file content
        findViewById(R.id.txt_read_output).setOnClickListener(v -> {
            try {
                String text = readFile(new FileInputStream(
                        getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/copy.txt"));
                mTxtOutputResult.setText(text);
            } catch (FileNotFoundException e) {
                Log.e("UNA", "txt_read_output: " + e.getMessage());
            }
        });
        mTxtOutputResult = findViewById(R.id.txt_output_result);
    }

    private String readFile(InputStream inputStream) {
        byte[] bytes = new byte[128];
        String txt = "";
        int length;
        try (InputStream input = inputStream;
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            while ((length = input.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, length);
            }
            txt = byteArrayOutputStream.toString();
        } catch (IOException e) {
            Log.e("UNA", "readFile: " + e.getMessage());
        }
        return txt;
    }

    private void outputFile(String text) {
        File path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        Log.e("UNA", "outputFile: file path " + path);
        File outputFile = new File(path, "copy.txt");
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(text.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            Log.e("UNA", "outputFile: " + e.getMessage());
        }
    }
}
