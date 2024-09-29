package com.example.notesapp;

import java.util.List;

public class GrammarResponse {
    public List<Match> matches;

    public static class Match {
        public int offset;
        public int length;
        public String message;
        public List<Replacement> replacements; // Suggestions to correct the error

        public static class Replacement {
            public String value; // The suggested correction

        }
    }
}