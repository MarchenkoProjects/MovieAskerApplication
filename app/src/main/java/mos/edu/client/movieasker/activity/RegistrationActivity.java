package mos.edu.client.movieasker.activity;

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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.activity.dialog.DialogManager;
import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.db.DaoSession;
import mos.edu.client.movieasker.db.User;
import mos.edu.client.movieasker.db.UserDao;
import mos.edu.client.movieasker.dto.UserDTO;

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

        if (login.isEmpty()) {
            Toast.makeText(this, R.string.login_empty_warning, Toast.LENGTH_SHORT).show();
        }
        else if (login.length() < MIN_LOGIN_LENGTH) {
            Toast.makeText(this, R.string.login_length_warning, Toast.LENGTH_SHORT).show();
        }
        else if (password.isEmpty()) {
            Toast.makeText(this, R.string.password_empty_warning, Toast.LENGTH_SHORT).show();
        }
        else if (password.length() < MIN_PASSWORD_LENGTH) {
            Toast.makeText(this, R.string.password_length_warning, Toast.LENGTH_SHORT).show();
        }
        else if (repeatPassword.isEmpty()) {
            Toast.makeText(this, R.string.repeat_password_empty_warning, Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(repeatPassword)) {
            Toast.makeText(this, R.string.passwords_not_equals_warning, Toast.LENGTH_SHORT).show();
        }
        else if (email.isEmpty()) {
            Toast.makeText(this, R.string.email_empty_warning, Toast.LENGTH_SHORT).show();
        }
        else {
            final String passwordCrypt = getMD5Hash(password);
            final UserDTO user = new UserDTO(login, passwordCrypt, email);
            new RegistrationTask(this).execute(user);
        }
    }

    private void loadViews() {
        initToolbar();

        stateRegistrationProgressBar = (ProgressBar) findViewById(R.id.state_registration_progressbar);
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
            }
            else if (length >= MIN_PASSWORD_LENGTH && length < NORMAL_PASSWORD_LENGTH) {
                passwordEditText.setBackgroundResource(R.color.color_yellow);
            }
            else {
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
            }
            else {
                final String password = passwordEditText.getText().toString();
                final String repeatPassword = repeatPasswordEditText.getText().toString();
                if (password.equals(repeatPassword)) {
                    repeatPasswordEditText.setBackgroundResource(R.color.color_green);
                }
                else {
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

    private static class RegistrationTask extends AsyncTask<UserDTO, Void, Integer> {
        public static final int BAD_INTERNET_CONNECTION_CODE = -1;
        public static final int OK_CODE = 0;
        public static final int LOGIN_EXISTS_CODE = 1;

        private final RegistrationActivity registrationActivity;
        private final RestTemplate template = ThisApplication.getInstance().getRestTemplate();

        public RegistrationTask(RegistrationActivity registrationActivity) {
            this.registrationActivity = registrationActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registrationActivity.stateRegistrationProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(UserDTO... users) {
            final UserDTO user = users[0];

            final ResponseEntity<UserDTO> response;
            try {
                final boolean loginFound = checkUserLogin(user.getLogin());
                if (loginFound) {
                    return LOGIN_EXISTS_CODE;
                }

                response = template.postForEntity(Constants.URI.USERS, user, UserDTO.class);
            } catch (RestClientException rce) {
                return BAD_INTERNET_CONNECTION_CODE;
            }

            if (response.getStatusCode() == HttpStatus.CREATED) {
                final UserDTO createdUser = response.getBody();

                final User localUser = new User();
                localUser.setGlobalId(createdUser.getIdUser());
                localUser.setLogin(createdUser.getLogin());
                localUser.setPassword(user.getPassword());
                localUser.setEmail(createdUser.getEmail());

                final DaoSession session = ThisApplication.getInstance().getSession();
                final UserDao userDao = session.getUserDao();
                userDao.insert(localUser);

                return OK_CODE;
            }

            return BAD_INTERNET_CONNECTION_CODE;
        }

        @Override
        protected void onPostExecute(Integer resultCode) {
            registrationActivity.stateRegistrationProgressBar.setVisibility(View.GONE);

            switch (resultCode) {
                case LOGIN_EXISTS_CODE:
                    Toast.makeText(registrationActivity, R.string.login_exists_warning, Toast.LENGTH_LONG).show();
                    break;
                case BAD_INTERNET_CONNECTION_CODE:
                    DialogManager.showDialog(registrationActivity, DialogManager.CREATE_USER_FAILED_DIALOG);
                    break;
                case OK_CODE:
                    DialogManager.showDialog(registrationActivity, DialogManager.CREATE_USER_SUCCESSFUL_DIALOG);
                    break;
            }

            super.onPostExecute(resultCode);
        }

        private boolean checkUserLogin(String login) throws RestClientException {
            final ResponseEntity<UserDTO> response =
                    template.getForEntity(Constants.URI.USER_BY_LOGIN, UserDTO.class, login);
            return response.getStatusCode() == HttpStatus.OK;
        }

    }

}
