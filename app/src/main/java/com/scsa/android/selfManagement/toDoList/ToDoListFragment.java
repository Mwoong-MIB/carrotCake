package com.scsa.android.selfManagement.toDoList;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.scsa.android.selfManagement.R;

import java.util.ArrayList;

public class ToDoListFragment extends Fragment {

    RecyclerView recyclerView;
    TaskAdapter adapter;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    protected Context toDoListActivityViewContext;

    public ToDoListFragment(){
        super();
    }
    public ToDoListFragment(Context context) {
        this.toDoListActivityViewContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_to_do_list, container, false);

        initUI(rootView);
        loadTaskListData();

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTaskListData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return rootView;
    }

    private void initUI(ViewGroup rootView) {
        recyclerView = rootView.findViewById(R.id.recyclerToDoList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TaskAdapter(this.toDoListActivityViewContext);
        recyclerView.setAdapter(adapter);
    }

    public int loadTaskListData(){

        String loadSql = "select _id, TODO from " + TaskDatabase.TABLE_TASK + " order by _id asc";

        int recordCount = -1;
        TaskDatabase database = TaskDatabase.getInstance(context);

        if(database != null){
            Cursor outCursor = database.rawQuery(loadSql);
            Log.i("msg","===============" + outCursor.toString());
            recordCount = outCursor.getCount();

            ArrayList<Task> items = new ArrayList<>();

            for(int i = 0; i < recordCount; i++){
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                String todo = outCursor.getString(1);
                items.add(new Task(_id,todo));
            }

            outCursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();

            return recordCount;
        } else return -1;

    }

}
