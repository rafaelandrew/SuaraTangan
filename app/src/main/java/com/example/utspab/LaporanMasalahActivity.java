package com.example.utspab;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LaporanMasalahActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_masalah);

        ImageView ivBack = findViewById(R.id.iv_back_lapor);
        EditText etJudul = findViewById(R.id.et_judul_masalah);
        EditText etDeskripsi = findViewById(R.id.et_deskripsi_masalah);
        Button btnKirim = findViewById(R.id.btn_kirim_laporan);

        ivBack.setOnClickListener(v -> finish());

        btnKirim.setOnClickListener(v -> {
            String judul = etJudul.getText().toString().trim();
            String deskripsi = etDeskripsi.getText().toString().trim();

            if(judul.isEmpty() || deskripsi.isEmpty()){
                Toast.makeText(this, "Mohon lengkapi laporan Anda", Toast.LENGTH_SHORT).show();
            } else {
                submitLaporan(judul, deskripsi);
            }
        });
    }

    private void submitLaporan(String judul, String deskripsi) {
        SharedPreferences prefs = getSharedPreferences("GoScorePrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Error: User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConfig.SUBMIT_LAPORAN_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.optString("message", "");

                        if ("success".equals(status)) {
                            Toast.makeText(this, "Laporan terkirim! Terima kasih.", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error submitting report", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("judul", judul);
                params.put("deskripsi", deskripsi);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}