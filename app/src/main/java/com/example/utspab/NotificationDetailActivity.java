package com.example.utspab;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        ImageView ivBack = findViewById(R.id.iv_back_detail);
        TextView tvTitleHeader = findViewById(R.id.tv_title_detail);
        TextView tvDetailTitle = findViewById(R.id.tv_detail_title);
        TextView tvDetailContent = findViewById(R.id.tv_detail_content);

        String title = getIntent().getStringExtra("NOTIFICATION_TITLE");
        String content = getIntent().getStringExtra("NOTIFICATION_CONTENT");

        tvTitleHeader.setText(title);

        tvDetailTitle.setVisibility(android.view.View.GONE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvDetailContent.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvDetailContent.setText(Html.fromHtml(content));
        }

        ivBack.setOnClickListener(v -> finish());
    }
}