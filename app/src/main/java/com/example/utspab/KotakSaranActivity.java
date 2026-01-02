package com.example.utspab;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class KotakSaranActivity extends AppCompatActivity {

    private EditText etSaran;
    private AppCompatButton btnKirim;
    private ImageView btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kotak_saran);

        // Match XML IDs EXACTLY
        etSaran = findViewById(R.id.et_isi_saran);
        btnKirim = findViewById(R.id.btn_kirim_saran);
        btnBack = findViewById(R.id.iv_back_saran);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Submit button
        btnKirim.setOnClickListener(v -> {
            String saran = etSaran.getText().toString().trim();

            if (saran.isEmpty()) {
                Toast.makeText(this, "Saran tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Terima kasih atas saran Anda", Toast.LENGTH_SHORT).show();
            etSaran.setText("");
        });
    }
}
