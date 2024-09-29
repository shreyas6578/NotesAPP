package com.example.notesapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton add;
    RecycleAdapter recycleAdapter;
    RecyclerView recyclerView;
    DataHelper dataHelper;
    List<database> noteList;
    ImageButton menus;

    @Override
    protected void onStart(){
    super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);
        boolean isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true); // Use correct key
        if (!isLoggedIn) {
            Intent intents = new Intent(this, Login.class);
            startActivity(intents);

            finish();
        }

        if (isFirstLaunch) {
            Intent intent = new Intent(MainActivity.this, splash_screen.class); // Use the correct class name
            startActivity(intent);
            finish();

            // Update preference to indicate that the app has been launched
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstLaunch", false);
            editor.apply();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        add = findViewById(R.id.floatingActionButton2);
        add.setOnClickListener(click -> {
            Intent intent = new Intent(this, write_note.class);
            Toast.makeText(this, "open Editor", Toast.LENGTH_SHORT).show();
            startActivity(intent);

        });

        dataHelper = DataHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyle_view); // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager

        new Thread(this::load).start();
        menus = findViewById(R.id.imageButton3);
        menus.setOnClickListener(click->{
            Intent intent = new Intent(this, menus.class);
            Toast.makeText(this, "open setting", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });


    }

    public void load() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Fetch data on a background thread
            noteList = dataHelper.getAllNotes();

            // Update UI on the main thread
            runOnUiThread(() -> {
                recycleAdapter = new RecycleAdapter(noteList);
                recyclerView.setAdapter(recycleAdapter);
            });
        });
    }
}