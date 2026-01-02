package com.example.utspab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private TextInputLayout tilNama, tilUsername, tilEmail, tilPassword, tilKonfirmasiPassword, tilTelepon;
    private TextInputEditText etNama, etUsername, etEmail, etPassword, etKonfirmasiPassword, etTelepon;
    private CheckBox cbPersetujuan;
    private Button btnRegister;
    private ImageView ivBack;
    private RadioGroup rgPeran;
    private TextView tvLoginLink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tilNama = view.findViewById(R.id.til_nama);
        tilUsername = view.findViewById(R.id.til_username);
        tilEmail = view.findViewById(R.id.til_email);
        tilTelepon = view.findViewById(R.id.til_telepon);
        tilPassword = view.findViewById(R.id.til_password);
        tilKonfirmasiPassword = view.findViewById(R.id.til_konfirmasi);

        etNama = view.findViewById(R.id.et_nama_lengkap);
        etUsername = view.findViewById(R.id.et_username);
        etEmail = view.findViewById(R.id.et_email_daftar);
        etTelepon = view.findViewById(R.id.et_nomor_telepon);
        etPassword = view.findViewById(R.id.et_password_daftar);
        etKonfirmasiPassword = view.findViewById(R.id.et_konfirmasi_password);

        cbPersetujuan = view.findViewById(R.id.cb_persetujuan);
        btnRegister = view.findViewById(R.id.btn_register);
        ivBack = view.findViewById(R.id.iv_register_back);
        rgPeran = view.findViewById(R.id.rg_peran);
        tvLoginLink = view.findViewById(R.id.tv_login_link);

        btnRegister.setOnClickListener(v -> validateAndRegister());

        ivBack.setOnClickListener(v -> {
            if (getActivity() != null) getActivity().getSupportFragmentManager().popBackStack();
        });

        setupTermsLink(cbPersetujuan);
        setupLoginLink(tvLoginLink);
    }
    private void setupLoginLink(TextView tvLogin) {
        String text = "Sudah punya akun? Masuk";
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (getActivity() instanceof AuthActivity) {
                    ((AuthActivity) getActivity()).navigateToLogin(null);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setFakeBoldText(true);
                ds.setColor(Color.parseColor("#2c5f6f"));
            }
        };

        int startIndex = text.indexOf("Masuk");
        if (startIndex != -1) {
            ss.setSpan(clickableSpan, startIndex, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvLogin.setText(ss);
        tvLogin.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setupTermsLink(CheckBox cb) {
        String text = cb.getText().toString();
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(getActivity(), TermsActivity.class));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(Color.parseColor("#2c5f6f"));
                ds.setFakeBoldText(true);
            }
        };

        int startIndex = text.indexOf("Syarat");
        if (startIndex != -1) {
            ss.setSpan(clickableSpan, startIndex, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        cb.setText(ss);
        cb.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void validateAndRegister() {
        tilNama.setError(null); tilUsername.setError(null); tilEmail.setError(null);
        tilPassword.setError(null); tilKonfirmasiPassword.setError(null); tilTelepon.setError(null);
        cbPersetujuan.setTextColor(Color.BLACK);

        String nama = etNama.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String konfirmasiPassword = etKonfirmasiPassword.getText().toString().trim();
        String telepon = etTelepon.getText().toString().trim();

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

        if (username.isEmpty()) {
            tilUsername.setError("Username tidak boleh kosong");
            return;
        }
        if (username.length() < 4 || username.length() > 16) {
            tilUsername.setError("Username harus 4-16 karakter");
            return;
        }
        if (!username.matches("[a-zA-Z0-9]+")) {
            tilUsername.setError("Username hanya boleh huruf dan angka");
            return;
        }

        if (email.isEmpty()) {
            tilEmail.setError("Email tidak boleh kosong");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Format email tidak valid");
            return;
        }

        if (telepon.isEmpty()) {
            tilTelepon.setError("Nomor Telepon tidak boleh kosong");
            return;
        }
        if (!Patterns.PHONE.matcher(telepon).matches() || telepon.length() < 10) {
            tilTelepon.setError("Format nomor telepon tidak valid");
            return;
        }

        if (password.isEmpty()) {
            tilPassword.setError("Password tidak boleh kosong");
            return;
        }
        if (password.length() < 6 || password.length() > 16) {
            tilPassword.setError("Password harus 6-16 karakter");
            return;
        }
        boolean adaHuruf = password.matches(".*[a-zA-Z].*");
        boolean adaAngka = password.matches(".*[0-9].*");
        boolean adaSimbol = password.matches(".*[^a-zA-Z0-9].*");

        if (!adaHuruf || !adaAngka || !adaSimbol) {
            tilPassword.setError("Password harus kombinasi huruf, angka, dan simbol");
            return;
        }

        if (konfirmasiPassword.isEmpty()) {
            tilKonfirmasiPassword.setError("Konfirmasi Password tidak boleh kosong");
            return;
        }
        if (!password.equals(konfirmasiPassword)) {
            tilKonfirmasiPassword.setError("Password tidak sama");
            return;
        }

        int selectedPeranId = rgPeran.getCheckedRadioButtonId();
        if (selectedPeranId == -1) {
            Toast.makeText(getContext(), "Pilih peran Anda (Teman Tuli/Dengar)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbPersetujuan.isChecked()) {
            cbPersetujuan.setTextColor(Color.RED);
            Toast.makeText(getContext(), "Setujui Syarat & Ketentuan", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton rbPeran = getView().findViewById(selectedPeranId);
        String peranStr = rbPeran.getText().toString();

        performRegister(nama, username, email, telepon, password, peranStr);
    }

    private void performRegister(String nama, String username, String email, String telepon, String password, String peran) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConfig.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if ("success".equals(status)) {
                                Toast.makeText(getContext(), "Registrasi Berhasil!", Toast.LENGTH_SHORT).show();

                                Bundle bundle = new Bundle();
                                bundle.putString("USER_CREDENTIAL", username);
                                bundle.putString("NAMA_LENGKAP", nama);
                                bundle.putString("EMAIL", email);
                                bundle.putString("TELEPON", telepon);
                                bundle.putString("PERAN_PENGGUNA", peran);

                                if (getActivity() instanceof AuthActivity) {
                                    ((AuthActivity) getActivity()).navigateToLogin(bundle);
                                }
                            } else {
                                String message = jsonObject.optString("message", "Registration failed");
                                if (message.contains("Username") || message.contains("username")) {
                                    tilUsername.setError(message);
                                } else if (message.contains("Email") || message.contains("email")) {
                                    tilEmail.setError(message);
                                } else if (message.contains("Telepon") || message.contains("telepon")) {
                                    tilTelepon.setError(message);
                                } else if (message.contains("Password") || message.contains("password")) {
                                    tilPassword.setError(message);
                                } else if (message.contains("Nama") || message.contains("nama")) {
                                    tilNama.setError(message);
                                } else {
                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Registration failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lengkap", nama);
                params.put("username", username);
                params.put("email", email);
                params.put("telepon", telepon);
                params.put("password", password);
                params.put("peran", peran);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}