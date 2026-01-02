package com.example.utspab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvNotifications = view.findViewById(R.id.rv_notifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Notification> notificationList = new ArrayList<>();


        notificationList.add(new Notification("Fitur Baru", R.drawable.ic_new_feature));
        notificationList.add(new Notification("Update Aplikasi", R.drawable.ic_system_update));
        notificationList.add(new Notification("Tips & Trik", R.drawable.ic_notif));
        notificationList.add(new Notification("Event Komunitas", R.drawable.ic_notifications));
        notificationList.add(new Notification("Kamus Diperbarui", R.drawable.ic_notif));
        notificationList.add(new Notification("Feedback Anda", R.drawable.ic_profile));
        notificationList.add(new Notification("Promo Spesial", R.drawable.ic_notif));
        notificationList.add(new Notification("Info Pemeliharaan", R.drawable.ic_system_update));
        notificationList.add(new Notification("Kuis Mingguan", R.drawable.ic_home));

        NotificationAdapter adapter = new NotificationAdapter(notificationList);
        rvNotifications.setAdapter(adapter);
    }
}
