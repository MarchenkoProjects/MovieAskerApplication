package mos.edu.client.movieasker.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import junit.framework.Assert;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.dto.UserDTO;
import mos.edu.client.movieasker.task.RegistrationTask;

public class RegistrationActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.activity_registration;

    private static final int MIN_LOGIN_LENGTH = 3;
    private static final int MIN_PASSWORD_LENGTH = 4;
    private static final int NORMAL_PASSWORD_LENGTH = 8;

    private EditText loginEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private EditText emailEditText;
    private ProgressBar stateProgressBar;

    private RegistrationTask registrationTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        loadViews();

        checkRegistrationRunning();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        if (registrationTask != null && registrationTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            return registrationTask;
        }
        return null;
    }

    public void createUserClick(View view) {
        final String login = loginEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String repeatPassword = repeatPasswordEditText.getText().toString();
        final String email = emailEditText.getText().toString();

        if (login.isEmpty()) {
            Toast.makeText(this, R.string.login_empty_warning, Toast.LENGTH_SHORT).show();
        } else if (login.length() < MIN_LOGIN_LENGTH) {
            Toast.makeText(this, R.string.login_length_warning, Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, R.string.password_empty_warning, Toast.LENGTH_SHORT).show();
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            Toast.makeText(this, R.string.password_length_warning, Toast.LENGTH_SHORT).show();
        } else if (repeatPassword.isEmpty()) {
            Toast.makeText(this, R.string.repeat_password_empty_warning, Toast.LENGTH_SHORT).show();
        } else if (!password.equals(repeatPassword)) {
            Toast.makeText(this, R.string.passwords_not_equals_warning, Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, R.string.email_empty_warning, Toast.LENGTH_SHORT).show();
        } else {
            final String passwordCrypt = getMD5Hash(password);
            final UserDTO user = new UserDTO(login, passwordCrypt, email);
            new RegistrationTask(this).execute(user);
        }
    }

    public void showStateProgressBar(boolean isShow) {
        if (isShow) {
            stateProgressBar.setVisibility(View.VISIBLE);
        } else {
            stateProgressBar.setVisibility(View.GONE);
        }
    }

    private void loadViews() {
        initToolbar();

        stateProgressBar = (ProgressBar) findViewById(R.id.state_registration_progressbar);
        loginEditText = (EditText) findViewById(R.id.login_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        if (passwordEditText != null) {
            passwordEditText.addTextChangedListener(controlPasswordLengthListener);
        }
        repeatPasswordEditText = (EditText) findViewById(R.id.repeat_password_edit_text);
        if (repeatPasswordEditText != null) {
            repeatPasswordEditText.addTextChangedListener(controlEqualsPasswordListener);
        }
        final CheckBox showPasswordCheckBox = (CheckBox) findViewById(R.id.show_password_checkbox);
        if (showPasswordCheckBox != null) {
            showPasswordCheckBox.setOnCheckedChangeListener(showPasswordListener);
        }
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.registration_toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.registration_title);
            setSupportActionBar(toolbar);
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private final TextWatcher controlPasswordLengthListener = new TextWatcher() {
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
                passwordEditText.setBackgroundResource(R.color.color_red);
            } else if (length >= MIN_PASSWORD_LENGTH && length < NORMAL_PASSWORD_LENGTH) {
                passwordEditText.setBackgroundResource(R.color.color_yellow);
            } else {
                passwordEditText.setBackgroundResource(R.color.color_green);
            }
        }
    };

    private final TextWatcher controlEqualsPasswordListener = new TextWatcher() {
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
                repeatPasswordEditText.setBackgroundResource(R.color.color_red);
            } else {
                final String password = passwordEditText.getText().toString();
                final String repeatPassword = repeatPasswordEditText.getText().toString();
                if (password.equals(repeatPassword)) {
                    repeatPasswordEditText.setBackgroundResource(R.color.color_green);
                } else {
                    repeatPasswordEditText.setBackgroundResource(R.color.color_red);
                }
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener showPasswordListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            if (checked) {
                passwordEditText.setTransformationMethod(null);
                repeatPasswordEditText.setTransformationMethod(null);
            } else {
                final PasswordTransformationMethod passwordTransformation =
                        new PasswordTransformationMethod();
                passwordEditText.setTransformationMethod(passwordTransformation);
                repeatPasswordEditText.setTransformationMethod(passwordTransformation);
            }
        }
    };

    private void checkRegistrationRunning() {
        final Object lastCustomInstance = getLastCustomNonConfigurationInstance();
        if (lastCustomInstance != null)  {
            registrationTask = (RegistrationTask) lastCustomInstance;
            if (registrationTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
                registrationTask.attachActivity(this);
            }
        }
    }

    private String getMD5Hash(String password) {
        final String HASH_ALGORITHM = "MD5";
        final int HASH_OUT_LENGTH = 32;
        final int RADIX = 16;

        String passwordCrypt = "";
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
            Assert.assertTrue("NoSuchAlgorithmException in getMD5Hash()", false);
        }

        return passwordCrypt;
    }

}
