package mos.edu.client.movieasker.activity.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.activity.MainActivity;
import mos.edu.client.movieasker.activity.RegistrationActivity;

public class DialogManager {
    public static final int BAD_INTERNET_CONNECTION = 0;
    public static final int CREATE_USER_SUCCESSFUL_DIALOG = 1;
    public static final int USER_NOT_REGISTERED = 2;

    public static AlertDialog createAndShowDialog(final Context context, int dialogId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        switch (dialogId) {
            case BAD_INTERNET_CONNECTION:
                return builder
                        .setTitle(R.string.dialog_warning_title)
                        .setMessage(R.string.dialog_bad_internet_connection_message)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();

            case CREATE_USER_SUCCESSFUL_DIALOG:
                return builder
                        .setTitle(R.string.dialog_info_title)
                        .setMessage(R.string.dialog_create_user_successful_message)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                                final AppCompatActivity activity = (AppCompatActivity) context;
                                NavUtils.navigateUpTo(activity, new Intent(activity, MainActivity.class));
                            }
                        })
                        .show();

            case USER_NOT_REGISTERED:
                return builder
                        .setTitle(R.string.dialog_warning_title)
                        .setMessage(R.string.dialog_user_not_registered_message)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_button_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                context.startActivity(new Intent(context, RegistrationActivity.class));
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.dialog_button_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();

            default:
                return null;
        }
    }

}
