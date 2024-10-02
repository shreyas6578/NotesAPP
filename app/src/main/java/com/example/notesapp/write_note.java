package com.example.notesapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class write_note extends AppCompatActivity {
    EditText title,notes;
    Button save,pdf;
    ImageButton check_grammer;
    DataHelper dataHelper;
    boolean edit = false;
    int noteId = -1; // Default value for note ID
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_write_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        onStart();
        check_grammer = findViewById(R.id.imageButton4);


// Set the onClickListener for the grammar check button
        check_grammer.setOnClickListener(v -> {
            String inputText = notes.getText().toString();
            notes = findViewById(R.id.editTextNote);
            if (!inputText.isEmpty()) {
                // Initialize Retrofit for LanguageTool API
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.languagetool.org/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                LanguageToolService service = retrofit.create(LanguageToolService.class);
                checkGrammar(service, inputText);
            } else {
                Toast.makeText(this, "Enter some text for grammar check", Toast.LENGTH_SHORT).show();
            }
        });

        dataHelper = DataHelper.getInstance(this);

        title = findViewById(R.id.editTextTitle);
        notes =findViewById(R.id.editTextNote);
        save =findViewById(R.id.button);
        pdf=findViewById(R.id.pdf_button);
        Intent intents =getIntent();
        if(intents!=null){
            //  String id = intents.getStringExtra("id");
            String titles = intents.getStringExtra("title");
            String note = intents.getStringExtra("note");
            title.setText(titles);
            notes.setText(note);
            noteId = intents.getIntExtra("id", -1);
            if (noteId != -1) {
                // We are in edit mode
                edit = true;
                title.setText(titles);
                notes.setText(note);
            }

        }
        pdf.setOnClickListener(click -> {
            String noteTitle = title.getText().toString();
            String noteText = notes.getText().toString();

            // Create a new PdfDocument
            PdfDocument pdfDocument = new PdfDocument();

            // Define page dimensions
            int pageWidth = 600;
            int pageHeight = 600;

            // Create a page description
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();

            // Start a page
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            // Set up Paint for drawing text
            Paint paint = new Paint();
            paint.setTextSize(16); // Set the title text size

            // Get the text width to center the title
            float titleWidth = paint.measureText("Title: " + noteTitle);
            float xTitle = (pageWidth - titleWidth) / 2;

            // Draw the centered title
            page.getCanvas().drawText("Title: " + noteTitle, xTitle, 40, paint);

            // Start drawing note text
            paint.setTextSize(14); // Set a smaller text size for the note content
            int yPosition = 60; // Starting y-position for the note text
            int lineHeight = 10; // Height of each line

            // Split note text into lines, preserving line breaks
            String[] noteLines = noteText.split("\n");
            for (String line : noteLines) {
                float lineWidth = paint.measureText(line);
                float xLine = (pageWidth - lineWidth) / 2; // Center each line
                page.getCanvas().drawText(line, xLine, yPosition, paint);
                yPosition += lineHeight; // Move to the next line
            }

            // Finish the page
            pdfDocument.finishPage(page);

            // Save the file using MediaStore
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            String fileName = noteTitle + "_" + System.currentTimeMillis() + ".pdf"; // Ensure unique file name
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);

            Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);

            if (uri != null) {
                try (OutputStream outputStream = resolver.openOutputStream(uri)) {
                    pdfDocument.writeTo(outputStream);
                    Toast.makeText(this, "PDF saved in Documents", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error creating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    pdfDocument.close();
                }
            } else {
                Toast.makeText(this, "Error creating PDF", Toast.LENGTH_SHORT).show();
            }
        });


        save.setOnClickListener(click->{
            new Thread(() -> {
                String noteTitle = title.getText().toString();
                String noteText = notes.getText().toString();
                String currentTime = getCurrentTime();
                if(edit && noteId != -1){
                    dataHelper.noteDao().updateNote(noteId,noteTitle,noteText,currentTime);
                }
                else {
                    // Create a new Note object
                    database note = new database(noteTitle, noteText, currentTime);

                    // Insert the note into the database
                    dataHelper.noteDao().insert(note);
                    runOnUiThread(() ->
                            Toast.makeText(this, "Note saved successfully!", Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
            Intent   intent = new Intent(this,MainActivity.class);
            startActivity(intent);

        });
    }
    public static String getCurrentTime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For API level 26 and above, use DateTimeFormatter
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return LocalDateTime.now().format(dtf);
        } else {
            // For older Android versions, use SimpleDateFormat
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }
    }
    private void highlightErrors(GrammarResponse grammarResponse, String text) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);

        // Highlight grammar mistakes in red
        for (GrammarResponse.Match match : grammarResponse.matches) {
            spannable.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.holo_red_dark)),
                    match.offset, match.offset + match.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Show a clickable span to provide suggestions
            spannable.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    showSuggestionsDialog(match.replacements);
                }
            }, match.offset, match.offset + match.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        notes.setText(spannable);
        notes.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showSuggestionsDialog(List<GrammarResponse.Match.Replacement> replacements) {
        String[] suggestions = new String[replacements.size()];
        for (int i = 0; i < replacements.size(); i++) {
            suggestions[i] = replacements.get(i).value;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Suggestions")
                .setItems(suggestions, (dialog, which) -> {
                    applySuggestion(replacements.get(which).value);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void applySuggestion(String suggestion) {
        Editable editable = notes.getText();
        int start = notes.getSelectionStart();
        int end = notes.getSelectionEnd();
        if (start >= 0 && end >= 0) {
            editable.replace(start, end, suggestion);
        }
    }

    private void checkGrammar(LanguageToolService service, String text) {
        Call<GrammarResponse> call = service.checkGrammar(text, "en");
        call.enqueue(new Callback<GrammarResponse>() {
            @Override
            public void onResponse(@NonNull Call<GrammarResponse> call, @NonNull Response<GrammarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GrammarResponse grammarResponse = response.body();
                    highlightErrors(grammarResponse, text);
                } else {
                    Toast.makeText(write_note.this, "Error checking grammar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GrammarResponse> call, Throwable t) {
                Toast.makeText(write_note.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
