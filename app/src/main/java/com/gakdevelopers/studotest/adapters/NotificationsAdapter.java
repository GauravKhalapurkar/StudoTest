package com.gakdevelopers.studotest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.models.NotificationsModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class NotificationsAdapter extends BaseAdapter {

    private List<NotificationsModel> notificationsList;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm aa");

    public NotificationsAdapter(List<NotificationsModel> notificationsList) {
        this.notificationsList = notificationsList;
    }

    @Override
    public int getCount() {
        return notificationsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;

        if (view == null) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item, viewGroup, false);
        } else {
            v = view;
        }

        TextView txtNotificationTitle = v.findViewById(R.id.txtNotificationTitle);
        TextView txtNotificationDesc = v.findViewById(R.id.txtNotificationDesc);
        TextView txtNotificationTime = v.findViewById(R.id.txtNotificationTime);

        txtNotificationTitle.setText("" + notificationsList.get(i).getTitle());
        txtNotificationDesc.setText("" + notificationsList.get(i).getDesc());
        txtNotificationTime.setText("" + sdf.format(notificationsList.get(i).getTime()));

        return v;
    }
}
