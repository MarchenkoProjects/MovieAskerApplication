package mos.edu.client.movieasker.activity.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

import mos.edu.client.movieasker.R;

public class DialogManager {
    public static final int CREATE_USER_FAILED_DIALOG = 0;
    public static final int CREATE_USER_SUCCESSFUL_DIALOG = 1;

    public static void showDialog(final Context context, int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        switch (id) {
            case CREATE_USER_FAILED_DIALOG:
                builder
                        .setTitle(R.string.dialog_create_user_failed_title)
                        .setMessage(R.string.dialog_create_user_failed_message)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                break;
            case CREATE_USER_SUCCESSFUL_DIALOG:
                builder
                        .setTitle(R.string.dialog_create_user_successful_title)
                        .setMessage(R.string.dialog_create_user_successful_message)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                NavUtils.navigateUpFromSameTask((AppCompatActivity) context);
                            }
                        })
                        .show();
                break;
        }
    }

}
