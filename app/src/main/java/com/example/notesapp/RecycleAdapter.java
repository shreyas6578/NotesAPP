package com.example.notesapp;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.HolderView> {

    private final List<database> noteList;

    // Constructor to initialize the note list
    public RecycleAdapter(List<database> noteList) {
        this.noteList = noteList;
    }

    // ViewHolder class to hold references to each item's views
    public static class HolderView extends RecyclerView.ViewHolder {
        TextView timeTextView, titleTextView, notesTextView;
        ImageView deleteButton ;
        ImageView open;
        public HolderView(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.Time);
            titleTextView = itemView.findViewById(R.id.Title);
            notesTextView = itemView.findViewById(R.id.note_view);
            deleteButton = itemView.findViewById(R.id.delete_btton);
            open = itemView.findViewById(R.id.open);
        }
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (e.g., note_item.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylce_view, parent, false);
        return new HolderView(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderView holder, int position) {
        // Get the current note from the list
        DataHelper dataHelper ;
      dataHelper = DataHelper.getInstance(holder.itemView.getContext());
        database currentNote = noteList.get(position);

        // Debug log to check data binding
        Log.d("RecycleAdapter", "Binding note: " + currentNote.getTlite());

        // Bind the data to the views in the ViewHolder
        holder.timeTextView.setText(currentNote.getTime());
        holder.titleTextView.setText(currentNote.getTlite());
        holder.notesTextView.setText(currentNote.getNotes());
        // Set up delete button click listener
        holder.deleteButton.setOnClickListener(click -> {
            new Thread(() -> {
                // Perform the delete operation on the background thread
                dataHelper.noteDao().delete(currentNote);
                // Remove the item from the list
                noteList.remove(position);
                // Post the RecyclerView update to the main thread
                holder.itemView.post(() -> notifyItemRemoved(position));
            }).start();
        });
        holder.open.setOnClickListener(click->{
            Intent intent=new Intent(holder.itemView.getContext(),write_note.class);
            intent.putExtra("id",currentNote.getId());
            intent.putExtra("title",currentNote.getTlite());
            intent.putExtra("note",currentNote.getNotes());
            holder.itemView.getContext().startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        // Return the size of the note list
        return noteList.size();
    }
}
