package com.scsa.android.selfManagement.helloWord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.scsa.android.selfManagement.R;

public class HelloWordActivityView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_word_view);
    }
    public void helloword_Start(View view) {
        Intent i = new Intent(this, HelloWordActivityStartView.class);
        startActivity(i);
    }

}