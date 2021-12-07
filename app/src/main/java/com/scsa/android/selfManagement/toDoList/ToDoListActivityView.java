package com.scsa.android.selfManagement.toDoList;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scsa.android.selfManagement.R;

public class ToDoListActivityView extends AppCompatActivity {

    public static TaskDatabase taskDatabase = null;

    static Fragment todoFragment;
    EditText inputToDo;
    Context maincontext;
    TextView timeView;
    Button timebutton;

    Handler handler;
    long timeBuff, millisecondTime, startTime, updateTime = 0L;
    int seconds, minutes, hour, milliSeconds;
    static public boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list_view);

        openDatabase();

        todoFragment = new ToDoListFragment(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,todoFragment).commit();

        inputToDo = findViewById(R.id.inputToDo);
        timeView = findViewById(R.id.timeView);
        timebutton = findViewById(R.id.timebutton);

        handler = new Handler();

        timebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == false){
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);

                    timebutton.setText("잠깐 쉴까요?");
                    Toast.makeText(ToDoListActivityView.this, "열심히 해보자구요!", Toast.LENGTH_SHORT).show();
                    flag = true;
                } else{
                    timeBuff += millisecondTime;
                    handler.removeCallbacks(runnable);

                    timebutton.setText("다시 집중할까요?");
                    Toast.makeText(ToDoListActivityView.this, "휴식도 생산이에요!", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,todoFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (taskDatabase != null) {
            taskDatabase.close();
            taskDatabase = null;
        }
        millisecondTime = 0L ;
        startTime = 0L ;
        timeBuff = 0L ;
        seconds = 0 ;
        minutes = 0 ;
        milliSeconds = 0 ;

        timeView.setText("00:00:00");
    }

    public void save(View view) {

        saveToDO();
        Toast.makeText(getApplicationContext(),"할 일이 늘었어요",Toast.LENGTH_SHORT).show();
        ((ToDoListFragment)todoFragment).loadTaskListData();
    }

    private void saveToDO() {
        String todo = inputToDo.getText().toString();

        String sqlSave = "insert into " + TaskDatabase.TABLE_TASK + " (TODO) values (" + "'" + todo + "')";

        TaskDatabase database = TaskDatabase.getInstance(maincontext);
        database.execSQL(sqlSave);

        inputToDo.setText("");
    }

    public void openDatabase() {

        if (taskDatabase != null) {
            taskDatabase.close();
            taskDatabase = null;
        }

        taskDatabase = TaskDatabase.getInstance(this);
        boolean isOpen = taskDatabase.open();
        if (isOpen) {
            Log.i("msg", "database is open.");
        } else {
            Log.d("msg", "database is not open.");
        }
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            millisecondTime = SystemClock.uptimeMillis() - startTime;

            updateTime = timeBuff + millisecondTime;

            seconds = (int) (updateTime / 1000);

            hour = minutes / 60;

            minutes = seconds / 60;

            seconds = seconds % 60;

            milliSeconds = (int) (updateTime % 1000);

            timeView.setText(""
                    + String.format("%02d", hour) + ":"
                    + String.format("%02d", minutes) + ":"
                    + String.format("%02d", seconds));

            handler.postDelayed(this, 0);
        }

    };
}