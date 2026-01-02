package com.example.utspab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class UbahSandiActivity extends AppCompatActivity {
    private TextInputLayout tilLama, tilBaru, tilKonfirmasi;
    private TextInputEditText etLama, etBaru, etKonfirmasi;
    private Button btnSimpan;
    private ImageView ivBack;
    private TextView tvLupaSandi;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_sandi);

        tilLama = findViewById(R.id.til_sandi_lama);
        tilBaru = findViewById(R.id.til_sandi_baru);
        tilKonfirmasi = findViewById(R.id.til_konfirmasi_sandi);
        etLama = findViewById(R.id.et_sandi_lama);
        etBaru = findViewById(R.id.et_sandi_baru);
        etKonfirmasi = findViewById(R.id.et_konfirmasi_sandi);
        btnSimpan = findViewById(R.id.btn_simpan_sandi);
        ivBack = findViewById(R.id.iv_back_sandi);
        tvLupaSandi = findViewById(R.id.tv_lupa_sandi);

        SharedPreferences prefs = getSharedPreferences("GoScorePrefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Error: User not found. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ivBack.setOnClickListener(v -> finish());

        tvLupaSandi.setOnClickListener(v ->
                Toast.makeText(this, "Fitur reset kata sandi akan segera hadir", Toast.LENGTH_SHORT).show()
        );

        btnSimpan.setOnClickListener(v -> validateAndChangePassword());
    }

    private void validateAndChangePassword() {
        tilLama.setError(null);
        tilBaru.setError(null);
        tilKonfirmasi.setError(null);

        String sandiLama = etLama.getText().toString().trim();
        String sandiBaru = etBaru.getText().toString().trim();
        String konfirmasi = etKonfirmasi.getText().toString().trim();

        if (sandiLama.isEmpty()) {
            tilLama.setError("Kata sandi lama tidak boleh kosong");
            return;
        }

        if (sandiBaru.isEmpty()) {
            tilBaru.setError("Kata sandi baru tidak boleh kosong");
            return;
        }

        if (sandiBaru.length() < 6) {
            tilBaru.setError("Password minimal 6 karakter");
            return;
        }

        boolean hasCapital = sandiBaru.matches(".*[A-Z].*");
        boolean hasSymbol = sandiBaru.matches(".*[^a-zA-Z0-9].*");

        if (!hasCapital || !hasSymbol) {
            tilBaru.setError("Password harus memiliki minimal 1 huruf besar dan 1 simbol");
            return;
        }

        if (konfirmasi.isEmpty()) {
            tilKonfirmasi.setError("Konfirmasi password tidak boleh kosong");
            return;
        }

        if (!sandiBaru.equals(konfirmasi)) {
            tilKonfirmasi.setError("Password tidak sama");
            return;
        }

        if (sandiLama.equals(sandiBaru)) {
            tilBaru.setError("Kata sandi baru harus berbeda dengan kata sandi lama");
            return;
        }

        performPasswordUpdate(sandiLama, sandiBaru, konfirmasi);
    }

    private void performPasswordUpdate(String currentPassword, String newPassword, String confirmPassword) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConfig.UPDATE_PASSWORD_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.optString("message", "");

                        if ("success".equals(status)) {
                            Toast.makeText(this, "Kata sandi berhasil diperbarui!", Toast.LENGTH_LONG).show();

                            // Clear the form
                            etLama.setText("");
                            etBaru.setText("");
                            etKonfirmasi.setText("");


                            finish();
                        } else {
                            // Show error message
                            if (message.contains("Current password") || message.contains("incorrect")) {
                                tilLama.setError(message);
                            } else if (message.contains("New password")) {
                                tilBaru.setError(message);
                            } else {
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Update failed: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("current_password", currentPassword);
                params.put("new_password", newPassword);
                params.put("confirm_password", confirmPassword);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}