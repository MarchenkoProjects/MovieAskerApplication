package mos.edu.client.movieasker.task;

import android.os.AsyncTask;
import android.widget.Toast;

import junit.framework.Assert;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.activity.RegistrationActivity;
import mos.edu.client.movieasker.activity.dialog.DialogManager;
import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.db.DaoSession;
import mos.edu.client.movieasker.db.User;
import mos.edu.client.movieasker.db.UserDao;
import mos.edu.client.movieasker.dto.UserDTO;

public class RegistrationTask extends AsyncTask<UserDTO, Void, Integer> {
    public static final int BAD_INTERNET_CONNECTION_CODE = -1;
    public static final int OK_CODE = 0;
    public static final int LOGIN_EXISTS_CODE = 1;

    private RegistrationActivity activity;

    public RegistrationTask(RegistrationActivity activity) {
        attachActivity(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showStateProgressBar(true);
    }

    @Override
    protected Integer doInBackground(UserDTO... users) {
        Assert.assertTrue("Users must be 1", users.length == 1);

        final RestTemplate template = ThisApplication.getInstance().getRestTemplate();
        final ResponseEntity<UserDTO> response;
        try {
            boolean loginFound = checkUserLogin(template, users[0].getLogin());
            if (loginFound) {
                return LOGIN_EXISTS_CODE;
            }

            response = template.postForEntity(Constants.URI.USERS, users[0], UserDTO.class);
        } catch (RestClientException rce) {
            return BAD_INTERNET_CONNECTION_CODE;
        }

        if (response.getStatusCode() == HttpStatus.CREATED) {
            final UserDTO createdUser = response.getBody();

            final User localUser = new User();
            localUser.setGlobalId(createdUser.getIdUser());
            localUser.setLogin(createdUser.getLogin());
            localUser.setPassword(users[0].getPassword());
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
        activity.showStateProgressBar(false);

        switch (resultCode) {
            case BAD_INTERNET_CONNECTION_CODE:
                DialogManager.createAndShowDialog(activity, DialogManager.BAD_INTERNET_CONNECTION);
                break;
            case LOGIN_EXISTS_CODE:
                Toast.makeText(activity, R.string.login_exists_warning, Toast.LENGTH_LONG).show();
                break;
            case OK_CODE:
                DialogManager.createAndShowDialog(activity, DialogManager.CREATE_USER_SUCCESSFUL_DIALOG);
                break;
        }

        super.onPostExecute(resultCode);
    }

    public void attachActivity(RegistrationActivity activity) {
        this.activity = activity;
    }

    private boolean checkUserLogin(RestTemplate template, String login) throws RestClientException {
        final ResponseEntity<UserDTO> response =
                template.getForEntity(Constants.URI.USER_BY_LOGIN, UserDTO.class, login);
        return response.getStatusCode() == HttpStatus.OK;
    }

}