package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes_table")
public class database {
    @NonNull
   @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
   @ColumnInfo(name = "title")
    private String tlite;
   @ColumnInfo(name = "Notes")
    private String Notes;
   @ColumnInfo(name = "Time")
    private String Time;

    public database(int id, String tlite, String Notes, String Time) {
        this.id = id;
        this.tlite = tlite;
        this.Notes = Notes;
        this.Time = Time;
    }
    @Ignore
    public database( String tlite, String Notes, String Time) {
        this.tlite = tlite;
        this.Notes = Notes;
        this.Time = Time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTlite() {
        return tlite;
    }

    public void setTlite(String tlite) {
        this.tlite = tlite;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
