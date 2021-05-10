package com.example.finalproject.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import com.example.finalproject.R;
import com.example.finalproject.SQL.DatabaseHelper;
import com.example.finalproject.modal.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ViewData extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = ViewData.this;
    private AppCompatTextView textViewName;
    private DatabaseHelper databaseHelper;
    private AppCompatButton appCompatButtonSubmit;
    private DatePicker dateViewer;
    private AppCompatTextView textViewSugar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initViews();
        initObjects();
        initListeners();
    }
    private void initViews() {
        dateViewer = (DatePicker) findViewById(R.id.dateViewer);
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        textViewSugar = (AppCompatTextView)findViewById(R.id.textViewSugar);
        appCompatButtonSubmit = (AppCompatButton) findViewById(R.id.appCompatButtonSubmit);
    }
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);
    }

    private void initListeners(){
        appCompatButtonSubmit.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.appCompatButtonSubmit) {
            retrieveDataFromSqlite();
            retrieveAllData();
        }
    }

    private void retrieveDataFromSqlite(){
        String selectedYear = String.valueOf(dateViewer.getYear());
        String selectedMonth = String.valueOf(dateViewer.getMonth() +1);
        String selectedDay =  String.valueOf(dateViewer.getDayOfMonth());

        String date = (selectedYear) + '-' + (selectedMonth) + '-' + selectedDay;
        String email = textViewName.getText().toString();

        Data dataSetter = new Data();
        dataSetter.setDate(date);
        dataSetter.setEmail(email);

        String x;
        x = databaseHelper.viewData(dataSetter);

        textViewSugar.setText(x);
    }

    private void retrieveAllData(){
        String selectedYear = String.valueOf(dateViewer.getYear());
        String selectedMonth = String.valueOf(dateViewer.getMonth() +1);
        String selectedDay =  String.valueOf(dateViewer.getDayOfMonth());

        String date = (selectedYear) + '-' + (selectedMonth) + '-' + selectedDay;
        String email = textViewName.getText().toString();

        Data dataSetter = new Data();
        dataSetter.setDate(date);
        dataSetter.setEmail(email);

        //databaseHelper db = new DatabaseHelper(this);
        DatabaseHelper db = new DatabaseHelper(this);
        ArrayList<HashMap<String, String>> userList = db.GetUsers(dataSetter);
        ListView lv = findViewById(R.id.user_list);
        ListAdapter adapter = new SimpleAdapter(ViewData.this, userList, R.layout.list_row,
                new String[]{"results"}, new int[]{R.id.name});
        lv.setAdapter(adapter);
    }
}