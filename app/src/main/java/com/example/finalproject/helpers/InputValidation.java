package com.example.finalproject.helpers;

import android.app.Activity;
import android.content.Context;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import java.util.Objects;

public class InputValidation {
    private final Context context;
    /**constructor
     * @param context
     */
    public InputValidation(Context context) {
        this.context = context;
    }
    /**method to check InputEditText filled */
    public boolean isInputEditTextFilled(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = Objects.requireNonNull(textInputEditText.getText()).toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return true;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return false;
    }
    /** method to check InputEditText has valid email */
    public boolean isInputEditTextEmail(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = Objects.requireNonNull(textInputEditText.getText()).toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return true;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return false;
    }
    public boolean isInputEditTextMatches(TextInputEditText textInputEditText1, TextInputEditText textInputEditText2, TextInputLayout textInputLayout, String message) {
        String value1 = Objects.requireNonNull(textInputEditText1.getText()).toString().trim();
        String value2 = Objects.requireNonNull(textInputEditText2.getText()).toString().trim();
        if (!value1.contentEquals(value2)) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText2);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    /**
     * method to Hide keyboard
     *
     * @param view
     */
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}