package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {

    DataController db = null;
    notesAdapter notes = null;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.notes_layout, container, false);
        db = new DataController(getContext());
        FloatingActionButton add = view.findViewById(R.id.fab);
        String m_Text;
        RecyclerView rv = view.findViewById(R.id.tasksRecyclerView);
        ArrayList<NotesModel> todo = new ArrayList<>();

        Cursor cur = db.retrieveTodoList();
        while(cur.moveToNext()){
            todo.add(new NotesModel(cur.getString(1), Integer.parseInt(cur.getString(0))));
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Title");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.insertTodo(input.getText().toString(), 0);
                        todo.add(new NotesModel(input.getText().toString(), 0));
                        System.out.println("notes array size : "+todo.size());
                        notes.setItems(todo);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        ColorDrawable background = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.delete));

        //delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                NotesModel deletedCourse = todo.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();
                todo.remove(viewHolder.getAdapterPosition());
                notes.notifyItemRemoved(viewHolder.getAdapterPosition());
                DataController db = new DataController(getContext().getApplicationContext());
                db.deleteItem(deletedCourse.getTask());
                Snackbar.make(rv, deletedCourse.getTask(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        todo.add(position, deletedCourse);
                        db.insertTodo(deletedCourse.getTask(), deletedCourse.getChecked());
                        notes.notifyItemInserted(position);

                    }
                }).show();
            }
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;

                if (dX > 0) { // Swiping to the right
                    background.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                            itemView.getBottom());

                } else if (dX < 0) { // Swiping to the left
                    background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else { // view is unSwiped
                    background.setBounds(0, 0, 0, 0);
                }
                background.draw(c);
            }
        }).attachToRecyclerView(rv);

        ColorDrawable editBG = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.edit));
        //edit
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit Item");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(todo.get(viewHolder.getAdapterPosition()).getTask());
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("TAG", "onClick: checked before editing: "+todo.get(viewHolder.getAdapterPosition()).checked+" "+
                                todo.get(viewHolder.getAdapterPosition()).task+" "+input.getText());
                        db.updateStatus(input.getText().toString(), todo.get(viewHolder.getAdapterPosition()).checked);
                        todo.set(viewHolder.getAdapterPosition(), new NotesModel(input.getText().toString(), 0));
                        System.out.println("notes array size : "+todo.size());
                        notes.setItems(todo);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        notes.setItems(todo);
                    }
                });

                builder.show();
            }
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;

                if (dX > 0) { // Swiping to the right
                    editBG.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                            itemView.getBottom());

                } else if (dX < 0) { // Swiping to the left
                    editBG.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else { // view is unSwiped
                    editBG.setBounds(0, 0, 0, 0);
                }
                editBG.draw(c);
            }

        }).attachToRecyclerView(rv);

        notes = new notesAdapter(getContext(), todo);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(notes);
        System.out.println("just before sendinf :"+todo.size()+" "+notes.getItemCount());
        notes.setItems(todo);
        return view;
    }

}
