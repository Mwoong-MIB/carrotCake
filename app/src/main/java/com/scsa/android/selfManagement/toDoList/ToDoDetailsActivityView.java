package com.scsa.android.selfManagement.toDoList;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.scsa.android.selfManagement.R;

import java.util.Calendar;

public class ToDoDetailsActivityView extends AppCompatActivity {

    EditText titleEt;
    EditText desctEt;
    EditText dueEt;
    Button saveBtn;

    Calendar cal = Calendar.getInstance();
    int year = 0;
    int month = 0;
    int day = 0;

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_details_view);

        titleEt = findViewById(R.id.todo_title);
        desctEt = findViewById(R.id.desct);
        dueEt = findViewById(R.id.due);
        saveBtn = findViewById(R.id.save);

        dueEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        ToDoDetailsActivityView.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dueEt.setText(year +"-" + pad(monthOfYear+1) + "-"+ pad(dayOfMonth));
                                ToDoDetailsActivityView.this.year  = year;
                                ToDoDetailsActivityView.this.month = monthOfYear;
                                ToDoDetailsActivityView.this.day   = dayOfMonth;
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            String TODO;
            String desct;
            String due;

            @Override
            public void onClick(View v) {
                TODO = titleEt.getText().toString();
                desct = desctEt.getText().toString();
                due = dueEt.getText().toString();

                if (titleEt.getText().toString().equals("") || desctEt.getText().toString().equals("") || dueEt.getText().toString().equals("") ) {
                    Toast.makeText(ToDoDetailsActivityView.this, "내용을 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    updateTODO(TODO);
                    Toast.makeText(ToDoDetailsActivityView.this, "응원해요", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            private void updateTODO(String todo) {
                String updateSql1 = "update " + TaskDatabase.TABLE_TASK + " set DESCT = '" + desct + "' where " + "  TODO = '" + TODO+"'";
                String updateSql2 = "update " + TaskDatabase.TABLE_TASK + " set TIME = '" + due + "' where " + "  TODO = '" + TODO+"'";

                TaskDatabase database = TaskDatabase.getInstance(ToDoDetailsActivityView.this);
                database.execSQL(updateSql1);
                database.execSQL(updateSql2);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent recI = getIntent();
        String TODO = recI.getStringExtra("taskData");
        if(TODO != null){
            titleEt.setText(TODO);
            titleEt.setEnabled(false);
            refreshView(TODO);
        }
    }

    public void refreshView(String TODO){
        String loadSql = "select * from " + TaskDatabase.TABLE_TASK + " where TODO = '" + TODO +"'";
        TaskDatabase database = TaskDatabase.getInstance(ToDoDetailsActivityView.this);
        if(database != null){
            Cursor outCursor = database.rawQuery(loadSql);
            outCursor.moveToNext();
            String dbDesct = outCursor.getString(2);
            String dbTime = outCursor.getString(4);

            desctEt.setText(dbDesct);
            dueEt.setText(dbTime);
            outCursor.close();
        }
    }
}