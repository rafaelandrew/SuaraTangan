package com.example.utspab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class InfoAkunActivity extends AppCompatActivity {
    // Display mode views (v1)
    private LinearLayout layoutDisplayMode;
    private TextView tvNama, tvEmail, tvTelepon, tvPeran;

    // Edit mode views (v2)
    private LinearLayout layoutEditMode;
    private TextInputLayout tilNama, tilTelepon;
    private TextInputEditText etNama, etTelepon;
    private TextView tvEmailEdit;
    private RadioGroup rgPeran;

    // Common views
    private Button btnEdit;
    private ImageView ivBack;

    private int userId;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_akun);

        // Initialize display mode views
        layoutDisplayMode = findViewById(R.id.layout_display_mode);
        tvNama = findViewById(R.id.tv_info_nama);
        tvEmail = findViewById(R.id.tv_info_email);
        tvTelepon = findViewById(R.id.tv_info_telepon);
        tvPeran = findViewById(R.id.tv_info_peran);

        // Initialize edit mode views
        layoutEditMode = findViewById(R.id.layout_edit_mode);
        tilNama = findViewById(R.id.til_info_nama);
        tilTelepon = findViewById(R.id.til_info_telepon);
        etNama = findViewById(R.id.et_info_nama);
        etTelepon = findViewById(R.id.et_info_telepon);
        tvEmailEdit = findViewById(R.id.tv_info_email_edit);
        rgPeran = findViewById(R.id.rg_info_peran);

        // Common views
        btnEdit = findViewById(R.id.btn_edit_info);
        ivBack = findViewById(R.id.iv_back_info);

        // Get user_id from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("GoScorePrefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Error: User not found. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initially show display mode
        setEditMode(false);

        // Load user data
        loadUserData();

        // Back button
        ivBack.setOnClickListener(v -> finish());

        // Edit/Save button
        btnEdit.setOnClickListener(v -> {
            if (!isEditMode) {
                // Switch to edit mode
                setEditMode(true);
            } else {
                // Save changes
                validateAndUpdate();
            }
        });
    }

    private void setEditMode(boolean enabled) {
        isEditMode = enabled;

        if (enabled) {
            // Show edit mode, hide display mode
            layoutDisplayMode.setVisibility(View.GONE);
            layoutEditMode.setVisibility(View.VISIBLE);
            btnEdit.setText("Simpan");

            // Copy data from display views to edit views
            etNama.setText(tvNama.getText().toString());
            etTelepon.setText(tvTelepon.getText().toString());
            tvEmailEdit.setText(tvEmail.getText().toString());

            // Set peran radio button - using exact comparison
            String peran = tvPeran.getText().toString().trim();
            if (peran.equals("Teman Dengar")) {
                ((RadioButton) findViewById(R.id.rb_teman_dengar)).setChecked(true);
            } else if (peran.equals("Teman Tuli")) {
                ((RadioButton) findViewById(R.id.rb_teman_tuli)).setChecked(true);
            }
        } else {
            // Show display mode, hide edit mode
            layoutDisplayMode.setVisibility(View.VISIBLE);
            layoutEditMode.setVisibility(View.GONE);
            btnEdit.setText("Edit");
        }
    }

    private void loadUserData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConfig.GET_USER_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");

                        if ("success".equals(status)) {
                            JSONObject user = jsonObject.getJSONObject("user");

                            // Populate display mode views
                            String namaLengkap = user.optString("nama_lengkap", "-");
                            String email = user.optString("email", "-");
                            String telepon = user.optString("telepon", "-");
                            String peran = user.optString("peran", "-");

                            tvNama.setText(namaLengkap);
                            tvEmail.setText(email);
                            tvTelepon.setText(telepon);
                            tvPeran.setText(peran);
                        } else {
                            Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void validateAndUpdate() {
        // Clear errors
        tilNama.setError(null);
        tilTelepon.setError(null);

        String nama = etNama.getText().toString().trim();
        String telepon = etTelepon.getText().toString().trim();

        // Validate nama - must start with capital letter
        if (nama.isEmpty()) {
            tilNama.setError("Nama Lengkap tidak boleh kosong");
            return;
        }

        boolean isKapitalValid = true;
        String[] words = nama.split(" ");
        for (String word : words) {
            if (word.length() > 0) {
                if (!Character.isUpperCase(word.charAt(0))) {
                    isKapitalValid = false;
                    break;
                }
            }
        }
        if (!isKapitalValid) {
            tilNama.setError("Gunakan huruf kapital di awal setiap unsur nama");
            return;
        }

        // Validate telepon - must be exactly 12 digits
        if (telepon.isEmpty()) {
            tilTelepon.setError("Nomor Telepon tidak boleh kosong");
            return;
        }
        if (!telepon.matches("\\d{12}")) {
            tilTelepon.setError("Nomor telepon harus 12 digit");
            return;
        }

        // Get selected peran
        int selectedPeranId = rgPeran.getCheckedRadioButtonId();
        if (selectedPeranId == -1) {
            Toast.makeText(this, "Pilih peran Anda", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use exact hardcoded values to match backend validation
        String peran;
        if (selectedPeranId == R.id.rb_teman_dengar) {
            peran = "Teman Dengar";
        } else if (selectedPeranId == R.id.rb_teman_tuli) {
            peran = "Teman Tuli";
        } else {
            Toast.makeText(this, "Pilih peran yang valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform update
        performUpdate(nama, telepon, peran);
    }

    private void performUpdate(String nama, String telepon, String peran) {
        // Debug log - using exact string that will be sent
        android.util.Log.d("InfoAkunActivity", "Updating profile - Nama: " + nama + ", Telepon: " + telepon + ", Peran: [" + peran + "] length: " + peran.length());

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConfig.UPDATE_PROFILE_URL,
                response -> {
                    // Debug response
                    android.util.Log.d("InfoAkunActivity", "Server response: " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.optString("message", "");

                        if ("success".equals(status)) {
                            Toast.makeText(this, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show();

                            // Update SharedPreferences
                            SharedPreferences prefs = getSharedPreferences("GoScorePrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();

                            if (jsonObject.has("user")) {
                                JSONObject user = jsonObject.getJSONObject("user");
                                editor.putString("NAMA_LENGKAP", user.optString("nama_lengkap", ""));
                                editor.putString("EMAIL", user.optString("email", ""));
                                editor.putString("TELEPON", user.optString("telepon", ""));
                                editor.putString("PERAN_PENGGUNA", user.optString("peran", ""));
                                editor.apply();
                            }

                            // Update display views with new data
                            tvNama.setText(nama);
                            tvTelepon.setText(telepon);
                            tvPeran.setText(peran);

                            // Switch back to display mode
                            setEditMode(false);
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            android.util.Log.e("InfoAkunActivity", "Update failed: " + message);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        android.util.Log.e("InfoAkunActivity", "JSON parse error: " + e.getMessage());
                    }
                },
                error -> {
                    String errorMsg = error.getMessage() != null ? error.getMessage() : "Unknown error";
                    Toast.makeText(this, "Update failed: " + errorMsg, Toast.LENGTH_SHORT).show();
                    android.util.Log.e("InfoAkunActivity", "Network error: " + errorMsg);
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("nama_lengkap", nama);
                params.put("telepon", telepon);
                params.put("peran", peran);

                // Debug log params
                android.util.Log.d("InfoAkunActivity", "Sending params: " + params.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                return headers;
            }
        };

        queue.add(stringRequest);
    }
}