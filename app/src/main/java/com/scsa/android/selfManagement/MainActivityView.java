package com.scsa.android.selfManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.scsa.android.selfManagement.helloWord.HelloWordActivityStartView;
import com.scsa.android.selfManagement.helloWord.HelloWordActivityView;
import com.scsa.android.selfManagement.newFeed.NewFeedViewActivity;
import com.scsa.android.selfManagement.toDoList.ToDoListActivityView;

public class MainActivityView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void todolist(View view) {
        Intent i = new Intent(this, ToDoListActivityView.class);
        startActivity(i);
    }

    public void newsFeed(View view) {
        Intent i = new Intent(this, NewFeedViewActivity.class);
        startActivity(i);
    }

    public void helloword(View view) {
        Intent i = new Intent(this, HelloWordActivityView.class);
        startActivity(i);
    }


}