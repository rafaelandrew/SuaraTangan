package com.example.utspab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class KotakSaranActivity extends AppCompatActivity {

    private EditText etSaran;
    private AppCompatButton btnKirim;
    private ImageView btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kotak_saran);

        etSaran = findViewById(R.id.et_isi_saran);
        btnKirim = findViewById(R.id.btn_kirim_saran);
        btnBack = findViewById(R.id.iv_back_saran);

        btnBack.setOnClickListener(v -> finish());

        btnKirim.setOnClickListener(v -> {
            String saran = etSaran.getText().toString().trim();

            if (saran.isEmpty()) {
                Toast.makeText(this, "Saran tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            submitSaran(saran);
        });
    }

    private void submitSaran(String saran) {
        SharedPreferences prefs = getSharedPreferences("GoScorePrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Error: User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ApiConfig.SUBMIT_SARAN_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.optString("message", "");

                        if ("success".equals(status)) {
                            Toast.makeText(this, "Terima kasih atas saran Anda", Toast.LENGTH_SHORT).show();
                            etSaran.setText("");
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error submitting suggestion", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("saran", saran);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}