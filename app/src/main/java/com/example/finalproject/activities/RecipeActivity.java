package com.example.finalproject.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;

public class RecipeActivity extends AppCompatActivity {
    private AppCompatActivity activity = RecipeActivity.this;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_recipies);
        getSupportActionBar().hide();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
