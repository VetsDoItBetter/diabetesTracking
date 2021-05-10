package com.example.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import com.example.finalproject.R;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private AppCompatTextView textViewName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initViews();
        initObjects();

        Button b1=(Button)findViewById(R.id.button1); //Enter Data Activity
        Button b2=(Button)findViewById(R.id.button2); //View Data Activity
        Button b3=(Button)findViewById(R.id.button3); //View Recipes Activity
        Button b4 = (Button) findViewById(R.id.button4); //View Accounts Activity
        Button b5 = (Button) findViewById(R.id.button5);  //Logout

        b1.setOnClickListener(v -> {
            Intent addSugar = new Intent(MainActivity.this, AddRecord.class);
            addSugar.putExtra("EMAIL", textViewName.getText().toString().trim());
            startActivity(addSugar);
        });
        b2.setOnClickListener(v -> {
            Intent viewSugar = new Intent(MainActivity.this, ViewData.class);
            viewSugar.putExtra("EMAIL", textViewName.getText().toString().trim());
            startActivity(viewSugar);
        });
        b3.setOnClickListener(v -> {
            Intent viewRecipe = new Intent(MainActivity.this, RecipeActivity.class);
            viewRecipe.putExtra("EMAIL", textViewName.getText().toString().trim());
            startActivity(viewRecipe);
        });

        b4.setOnClickListener(v -> {
            Intent accountView = new Intent(MainActivity.this, UsersListActivity.class);
            accountView.putExtra("EMAIL", textViewName.getText().toString().trim());
            startActivity(accountView);
        });

        b5.setOnClickListener(v -> {
            Intent logout = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logout);
        });
    }

    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
    }
    private void initObjects() {
        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);
    }
}