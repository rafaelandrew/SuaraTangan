package com.example.utspab;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailBantuanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bantuan);

        ImageView ivBack = findViewById(R.id.iv_back_detail);
        TextView tvTitle = findViewById(R.id.tv_title_detail);
        TextView tvContent = findViewById(R.id.tv_content_detail);

        ivBack.setOnClickListener(v -> finish());

        String title = getIntent().getStringExtra("TITLE");
        String content = getIntent().getStringExtra("CONTENT");

        if (title != null) {
            tvTitle.setText(title);
        }

        if (content != null) {
            tvContent.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
        }
    }
}