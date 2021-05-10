package com.example.finalproject.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import com.example.finalproject.R;
import com.example.finalproject.adapters.UsersRecyclerAdapter;
import com.example.finalproject.modal.User;
import com.example.finalproject.SQL.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

public class UsersListActivity extends AppCompatActivity {
    private final AppCompatActivity activity = UsersListActivity.this;
    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewUsers;
    private List<User> listUsers;
    private UsersRecyclerAdapter usersRecyclerAdapter;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initViews();
        initObjects();
        initListeners();
    }

    /**This method is to initialize views */
    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUsers);

    }
    /**This method is to initialize objects to be used */
    private void initObjects() {
        listUsers = new ArrayList<>();
        usersRecyclerAdapter = new UsersRecyclerAdapter(listUsers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(usersRecyclerAdapter);
        databaseHelper = new DatabaseHelper(activity);

        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);

        getDataFromSQLite();
    }

    private void initListeners(){


    }

    /**
     * This method is to get all user records from SQLite
     */
    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation does not block the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listUsers.clear();
                listUsers.addAll(databaseHelper.getAllUser());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                usersRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}