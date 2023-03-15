package com.gakdevelopers.studotest.internet;

import static com.gakdevelopers.studotest.database.DbQuery.g_selected_test_index;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.gakdevelopers.studotest.activities.Question;
import com.gakdevelopers.studotest.activities.Score;

public class NetworkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Common.isConnectedToInternet(context)) {
            new AlertDialog.Builder(context)
                    .setTitle("No Internet Connection")
                    .setMessage("Make sure you are connected to the internet!")
                    .setCancelable(false)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            onReceive(context, intent);
                        }
                    })
                    .show();
        }
    }


}
