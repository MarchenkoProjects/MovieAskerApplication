package mos.edu.client.movieasker.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        EditText loginEditText = (EditText) findViewById(R.id.login_edit_text);
        Assert.assertNotNull(loginEditText);
        final String login = loginEditText.getText().toString();

        EditText passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        Assert.assertNotNull(passwordEditText);
        final String password = passwordEditText.getText().toString();

        EditText repeatPasswordEditText = (EditText) findViewById(R.id.repeat_password_edit_text);
        Assert.assertNotNull(repeatPasswordEditText);
        final String repeatPassword = repeatPasswordEditText.getText().toString();

        EditText emailEditText = (EditText) findViewById(R.id.email_edit_text);
        Assert.assertNotNull(emailEditText);
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
            registrationTask = new RegistrationTask(this);
            registrationTask.execute(user);
        }
    }

    public void showStateProgressBar(boolean isShow) {
        ProgressBar stateProgressBar = (ProgressBar) findViewById(R.id.state_registration_progressbar);
        Assert.assertNotNull(stateProgressBar);

        if (isShow) {
            stateProgressBar.setVisibility(View.VISIBLE);
        } else {
            stateProgressBar.setVisibility(View.GONE);
        }
    }

    private void loadViews() {
        initToolbar();

        CheckBox showPasswordCheckBox = (CheckBox) findViewById(R.id.show_password_checkbox);
        Assert.assertNotNull(showPasswordCheckBox);
        showPasswordCheckBox.setOnCheckedChangeListener(showPasswordListener);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.registration_toolbar);
        Assert.assertNotNull(toolbar);
        toolbar.setTitle(R.string.registration_title);
    }

    private final CompoundButton.OnCheckedChangeListener showPasswordListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            PasswordTransformationMethod passwordTransformation = null;
            if (!checked) {
                passwordTransformation = new PasswordTransformationMethod();
            }

            EditText passwordEditText = (EditText) findViewById(R.id.password_edit_text);
            Assert.assertNotNull(passwordEditText);
            passwordEditText.setTransformationMethod(passwordTransformation);

            EditText repeatPasswordEditText = (EditText) findViewById(R.id.repeat_password_edit_text);
            Assert.assertNotNull(repeatPasswordEditText);
            repeatPasswordEditText.setTransformationMethod(passwordTransformation);
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
