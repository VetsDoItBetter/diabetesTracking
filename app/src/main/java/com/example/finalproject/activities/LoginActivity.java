package com.example.finalproject.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.finalproject.R;
import com.example.finalproject.helpers.InputValidation;
import com.example.finalproject.SQL.DatabaseHelper;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import 	androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.VideoView;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;
    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initViews();
        initListeners();
        initObjects();

        // Hook up the VideoView to our UI.
        videoBG = (VideoView) findViewById(R.id.videoView);

        // Build your video Uri
        Uri uri = Uri.parse("android.resource://" // First start with this,
                + getPackageName() // then retrieve your package name,
                + "/" // add a slash,
                + R.raw.water); //add your video resource. Make sure it is stored in the raw folder.

        // Set the new Uri to our VideoView
        videoBG.setVideoURI(uri);
        // Start the VideoView
        videoBG.start();

        // Set an OnPreparedListener for our VideoView. For more information about VideoViews,

        videoBG.setOnPreparedListener(mediaPlayer -> {
            mMediaPlayer = mediaPlayer;
            // We want our video to play over and over so we set looping to true.
            mMediaPlayer.setLooping(true);
            // We then seek to the current posistion if it has been set and play the video.
            if (mCurrentVideoPosition != 0) {
                mMediaPlayer.seekTo(mCurrentVideoPosition);
                mMediaPlayer.start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Capture the current video position and pause the video.
        mCurrentVideoPosition = mMediaPlayer.getCurrentPosition();
        videoBG.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restart the video when resuming the Activity
        videoBG.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // When the Activity is destroyed, release our MediaPlayer and set it to null.
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
    }
    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }
    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
    }
    /**
     * This implemented method is to listen the click on view
     *
     * @param
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }
    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite() {
        if (inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }
        if (databaseHelper.checkUser(Objects.requireNonNull(textInputEditTextEmail.getText()).toString().trim()
                , Objects.requireNonNull(textInputEditTextPassword.getText()).toString().trim())) {
            Intent accountsIntent = new Intent(activity, UsersListActivity.class);
            Intent mainActivity = new Intent(activity, MainActivity.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            mainActivity.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();

            startActivity(mainActivity);

        } else {
            // toast to show message that record is wrong
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }
    /** This method is to empty all input edit text */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}