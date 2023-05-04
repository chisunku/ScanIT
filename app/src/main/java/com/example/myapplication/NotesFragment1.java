//package com.example.myapplication;
//
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//
//public class NotesFragment1 extends Fragment implements DialogCloseListener{
//
//    private DatabaseHandler db;
//
//    private RecyclerView tasksRecyclerView;
//    private ToDoAdapter tasksAdapter;
//    private FloatingActionButton fab;
//
//    private List<ToDoModel> taskList;
//
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////        return super.onCreateView(inflater, container, savedInstanceState);
//        View view = inflater.inflate(R.layout.notes_layout, container, false);
//
//
//        db = new DatabaseHandler(this);
//        db.openDatabase();
//
//        tasksRecyclerView = view.findViewById(R.id.tasksRecyclerView);
//        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(tasksAdapter.getContext()));
//        tasksAdapter = new ToDoAdapter(db, getClass());
//        tasksRecyclerView.setAdapter(tasksAdapter);
//
//        ItemTouchHelper itemTouchHelper = new
//                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
//        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);
//
//        fab = view.findViewById(R.id.fab);
//
//        taskList = db.getAllTasks();
//        Collections.reverse(taskList);
//
//        tasksAdapter.setTasks(taskList);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
//            }
//        });
//        return view;
//    }
//
//    @Override
//    public void handleDialogClose(DialogInterface dialog){
//        taskList = db.getAllTasks();
//        Collections.reverse(taskList);
//        tasksAdapter.setTasks(taskList);
//        tasksAdapter.notifyDataSetChanged();
//    }
//}