package com.scsa.android.selfManagement.toDoList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import static com.scsa.android.selfManagement.toDoList.ToDoListActivityView.todoFragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scsa.android.selfManagement.R;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    public Context toDoListActivityViewContext;
    public TaskAdapter(){
        super();
    }
    public TaskAdapter(Context context) {
        this.toDoListActivityViewContext = context;
    }

    private ToDoDetailsActivityView toDoDetailsActivityView;
    ArrayList<Task> items = new ArrayList<>();

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.todolist_item, parent, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        Task item = items.get(position);
        holder.setItem(item);
        holder.setLayout();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout layoutTodo;
        CheckBox checkBox;
        Button deleteButton;
        Button editButton;
        Context context;


        public ViewHolder(View itemView, TaskAdapter adapter){
            super(itemView);
            layoutTodo = itemView.findViewById(R.id.layoutTodo);
            checkBox = itemView.findViewById(R.id.checkBox);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String TODO = (String) checkBox.getText();
                    deleteToDo(TODO);
                    Toast.makeText(v.getContext(),"고생하셨어요!",Toast.LENGTH_SHORT).show();
                    ((ToDoListFragment)todoFragment).loadTaskListData();
                }

                private void deleteToDo(String TODO){
                    String deleteSql = "delete from " + TaskDatabase.TABLE_TASK + " where " + "  TODO = '" + TODO+"'";
                    TaskDatabase database = TaskDatabase.getInstance(context);
                    database.execSQL(deleteSql);
                }
            });

            editButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String TODO = (String) checkBox.getText();
                    Intent i = new Intent(adapter.toDoListActivityViewContext, ToDoDetailsActivityView.class);
                    i.putExtra("taskData", TODO);
                    adapter.toDoListActivityViewContext.startActivity(i);
                }
            });

            checkBox.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String TODO = (String) checkBox.getText();
                    Intent i = new Intent(adapter.toDoListActivityViewContext, ToDoDetailsActivityView.class);
                    i.putExtra("taskData", TODO);
                    adapter.toDoListActivityViewContext.startActivity(i);

                    return false;
                }
            });
        }

        public void setItem(Task item) {
            checkBox.setText(item.getTodo());
        }

        public void setLayout() {
            layoutTodo.setVisibility(View.VISIBLE);
        }
    }

    public void setItems(ArrayList<Task> tasks){
        this.items = tasks;
    }
}
