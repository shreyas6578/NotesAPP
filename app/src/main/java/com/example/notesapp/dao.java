package com.example.notesapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface dao {
    @Insert
    void insert(database note);

    // Delete a specific note from the database
    @Delete
    void delete(database note);

    // Query to get all notes from the database

    // Query to get all notes from the database
    @Query("SELECT * FROM notes_table")
    List<database> getAllNotes();

    // Query to delete all notes from the database
    @Query("DELETE FROM notes_table")
    void deleteAll();

    @Query("DELETE FROM sqlite_sequence WHERE name='notes_table'")
    void resetAutoIncrement();

    @Query("UPDATE notes_table SET title = :title, notes = :notes, time = :time WHERE id = :id")
    void updateNote(int id, String title, String notes, String time);


}
