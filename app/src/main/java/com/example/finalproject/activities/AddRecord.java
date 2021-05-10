package com.example.finalproject.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.finalproject.R;
//import com.example.finalproject.helpers.InputValidation;
import com.example.finalproject.modal.Data;
import com.example.finalproject.SQL.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.textfield.TextInputLayout;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.DatePicker;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
//import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class AddRecord extends AppCompatActivity implements View.OnClickListener{
    private final AppCompatActivity activity = AddRecord.this;
    private AppCompatTextView textViewName;
    private Data data;
    private DatabaseHelper databaseHelper;
    private AppCompatButton appCompatButtonSubmit;
    private NestedScrollView nestedScrollView;
    private TextInputEditText textInputEditTextSugar;
    private DatePicker datePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_add_record);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initViews();
        initObjects();
        initListeners();
    }

    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        textInputEditTextSugar = (TextInputEditText) findViewById(R.id.textInputEditTextSugar);
        appCompatButtonSubmit = (AppCompatButton) findViewById(R.id.appCompatButtonSubmit);
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
    }

    private void initObjects() {
        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);
        //InputValidation inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);

        data = new Data();
    }
    private void initListeners(){
       appCompatButtonSubmit.setOnClickListener((View.OnClickListener) this);
    }

  @RequiresApi(api = Build.VERSION_CODES.O)

  @Override
    public void onClick(View v) {
      if (v.getId() == R.id.appCompatButtonSubmit) {
          postDataToSQLite();
      }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void postDataToSQLite() {

        int myNum = 0;
        String email = textViewName.getText().toString();
        data.setEmail(email);

        try {
            myNum = Integer.parseInt(Objects.requireNonNull(textInputEditTextSugar.getText()).toString());
        } catch (NumberFormatException ignored){}

        data.setData(myNum);

        String selectedYear = String.valueOf(datePicker.getYear());
        String selectedMonth = String.valueOf(datePicker.getMonth() +1);
        String selectedDay =  String.valueOf(datePicker.getDayOfMonth());
        String date = (selectedYear) + '-' + (selectedMonth) + '-' + selectedDay;

        if(myNum == 0){

            Snackbar.make(nestedScrollView, getString(R.string.sugar_no_entry), Snackbar.LENGTH_LONG).show();
        }else {
            data.setDate(date);
            databaseHelper.addData(data);

            if (myNum >= 240) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(2000);
                }
                Snackbar.make(nestedScrollView, getString(R.string.sugar_high), Snackbar.LENGTH_LONG).show();
            }

            if (myNum <= 70) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(2000);
                }
                Snackbar.make(nestedScrollView, getString(R.string.sugar_low), Snackbar.LENGTH_LONG).show();
            }
            if (myNum > 70 && myNum < 240) {
                Snackbar.make(nestedScrollView, getString(R.string.sugar_added), Snackbar.LENGTH_LONG).show();
            }

            textInputEditTextSugar.setText(null);//clears textView for next entry.
        }
    }
}