package mos.edu.client.movieasker.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.ThisApplication;
import mos.edu.client.movieasker.db.User;

public class RegistrationActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.activity_registration;

    private static final int MIN_LOGIN_LENGTH = 3;
    private static final int MIN_PASSWORD_LENGTH = 4;
    private static final int NORMAL_PASSWORD_LENGTH = 8;

    private ProgressBar stateRegistrationProgressBar;
    private EditText loginEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        loadViews();
    }

    public void createUserClick(View view) {
        final String login = loginEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String repeatPassword = repeatPasswordEditText.getText().toString();
        final String email = emailEditText.getText().toString();

        Context context = ThisApplication.getInstance();
        if (login.isEmpty()) {
            Toast.makeText(context, R.string.login_empty_warning, Toast.LENGTH_SHORT).show();
        }
        else if (login.length() < MIN_LOGIN_LENGTH) {
            Toast.makeText(context, R.string.login_length_warning, Toast.LENGTH_SHORT).show();
        }
        else if (password.isEmpty()) {
            Toast.makeText(context, R.string.password_empty_warning, Toast.LENGTH_SHORT).show();
        }
        else if (password.length() < MIN_PASSWORD_LENGTH) {
            Toast.makeText(context, R.string.password_length_warning, Toast.LENGTH_SHORT).show();
        }
        else if (repeatPassword.isEmpty()) {
            Toast.makeText(context, R.string.repeat_password_empty_warning, Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(repeatPassword)) {
            Toast.makeText(context, R.string.passwords_not_equals_warning, Toast.LENGTH_SHORT).show();
        }
        else if (email.isEmpty()) {
            Toast.makeText(context, R.string.email_empty_warning, Toast.LENGTH_SHORT).show();
        }
        else {
            final String passwordCrypt = getMD5Hash(password);
            final User user = new User(1L, null, login, passwordCrypt, email);
            new RegistrationTask().execute(user);
        }
    }

    private void loadViews() {
        initToolbar();

        stateRegistrationProgressBar = (ProgressBar) findViewById(R.id.state_registration_progressbar);
        loginEditText = (EditText) findViewById(R.id.login_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        passwordEditText.addTextChangedListener(controlPasswordLengthListener);
        repeatPasswordEditText = (EditText) findViewById(R.id.repeat_password_edit_text);
        repeatPasswordEditText.addTextChangedListener(controlEqualsPasswordListener);
        CheckBox showPasswordCheckBox = (CheckBox) findViewById(R.id.show_password_checkbox);
        showPasswordCheckBox.setOnCheckedChangeListener(showPasswordListener);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.registration_toolbar);
        toolbar.setTitle(R.string.registration_title);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private TextWatcher controlPasswordLengthListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            final int length = editable.length();

            if (length < MIN_PASSWORD_LENGTH) {
                passwordEditText.setBackgroundResource(R.color.colorRed);
            }
            else if (length >= MIN_PASSWORD_LENGTH && length < NORMAL_PASSWORD_LENGTH) {
                passwordEditText.setBackgroundResource(R.color.colorYellow);
            }
            else {
                passwordEditText.setBackgroundResource(R.color.colorGreen);
            }
        }
    };

    private TextWatcher controlEqualsPasswordListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            final int passwordLength = passwordEditText.length();
            final int repeatPasswordLength = editable.length();

            if (passwordLength != repeatPasswordLength) {
                repeatPasswordEditText.setBackgroundResource(R.color.colorRed);
            }
            else {
                final String password = passwordEditText.getText().toString();
                final String repeatPassword = repeatPasswordEditText.getText().toString();
                if (password.equals(repeatPassword)) {
                    repeatPasswordEditText.setBackgroundResource(R.color.colorGreen);
                }
                else {
                    repeatPasswordEditText.setBackgroundResource(R.color.colorRed);
                }
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener showPasswordListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            if (checked) {
                passwordEditText.setTransformationMethod(null);
                repeatPasswordEditText.setTransformationMethod(null);
            }
            else {
                final PasswordTransformationMethod passwordTransformation =
                        new PasswordTransformationMethod();
                passwordEditText.setTransformationMethod(passwordTransformation);
                repeatPasswordEditText.setTransformationMethod(passwordTransformation);
            }
        }
    };

    private String getMD5Hash(String password) {
        final String HASH_ALGORITHM = "MD5";
        final int HASH_OUT_LENGTH = 32;
        final int RADIX = 16;

        String passwordCrypt = null;
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            messageDigest.reset();
            messageDigest.update(password.getBytes());
            final BigInteger bigInt = new BigInteger(1, messageDigest.digest());
            passwordCrypt = bigInt.toString(RADIX);
            while (passwordCrypt.length() < HASH_OUT_LENGTH) {
                passwordCrypt = "0" + passwordCrypt;
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("App", "NoSuchAlgorithmException in getMD5Hash()");
        }

        return passwordCrypt;
    }

    private class RegistrationTask extends AsyncTask<User, Void, Integer> {
        public static final int BAD_INTERNET_CONNECTION_CODE = -1;
        public static final int OK_CODE = 0;
        public static final int LOGIN_FOUND_CODE = 1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            stateRegistrationProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(User... users) {
            return OK_CODE;
        }

        @Override
        protected void onPostExecute(Integer resultCode) {
            stateRegistrationProgressBar.setVisibility(View.GONE);
            super.onPostExecute(resultCode);
        }
    }

}
