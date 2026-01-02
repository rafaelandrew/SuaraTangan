package com.example.utspab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
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

public class LoginFragment extends Fragment {
    private TextInputLayout tilCredential, tilPassword;
    private TextInputEditText etCredential, etPassword;
    private Button btnLogin;
    private ImageView ivBack;
    private TextView tvRegisterLink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tilCredential = view.findViewById(R.id.til_login_credential);
        tilPassword = view.findViewById(R.id.til_login_password);
        etCredential = view.findViewById(R.id.et_login_credential);
        etPassword = view.findViewById(R.id.et_login_password);
        btnLogin = view.findViewById(R.id.btn_login);
        ivBack = view.findViewById(R.id.iv_login_back);
        tvRegisterLink = view.findViewById(R.id.tv_register_link);

        btnLogin.setOnClickListener(v -> validateAndLogin());

        ivBack.setOnClickListener(v -> {
            if (getActivity() != null) getActivity().getSupportFragmentManager().popBackStack();
        });

        setupRegisterLink(tvRegisterLink);
        checkBundle();
    }

    private void checkBundle() {
        if (getArguments() != null) {
            String credentialFromRegister = getArguments().getString("USER_CREDENTIAL");
            if (credentialFromRegister != null) {
                etCredential.setText(credentialFromRegister);
            }
        }
    }

    private void validateAndLogin() {
        tilCredential.setError(null);
        tilPassword.setError(null);

        String credential = etCredential.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (credential.isEmpty()) {
            tilCredential.setError("Username atau Email tidak boleh kosong");
            return;
        }

        boolean isEmail = credential.contains("@");
        if (isEmail) {
            if (!Patterns.EMAIL_ADDRESS.matcher(credential).matches()) {
                tilCredential.setError("Format email tidak valid");
                return;
            }
        }

        if (password.isEmpty()) {
            tilPassword.setError("Password tidak boleh kosong");
            return;
        }

        performLogin(credential, password);
    }

    private void performLogin(String credential, String password) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConfig.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if ("success".equals(status)) {

                                SharedPreferences prefs = requireActivity().getSharedPreferences("GoScorePrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();

                                editor.putString("LOGGED_IN_CREDENTIAL", credential);

                                Intent intent = new Intent(getActivity(), MainActivity.class);

                                if (jsonObject.has("user")) {
                                    JSONObject user = jsonObject.getJSONObject("user");

                                    if (user.has("id")) {
                                        int userId = user.getInt("id");
                                        editor.putInt("USER_ID", userId);
                                        intent.putExtra("USER_ID", userId);
                                    }

                                    if (user.has("nama_lengkap")) {
                                        String namaLengkap = user.getString("nama_lengkap");
                                        editor.putString("NAMA_LENGKAP", namaLengkap);
                                        intent.putExtra("NAMA_LENGKAP", namaLengkap);
                                    }
                                    if (user.has("email")) {
                                        String email = user.getString("email");
                                        editor.putString("EMAIL", email);
                                        intent.putExtra("EMAIL", email);
                                    }
                                    if (user.has("telepon")) {
                                        String telepon = user.getString("telepon");
                                        editor.putString("TELEPON", telepon);
                                        intent.putExtra("TELEPON", telepon);
                                    }
                                    if (user.has("peran")) {
                                        String peran = user.getString("peran");
                                        editor.putString("PERAN_PENGGUNA", peran);
                                        intent.putExtra("PERAN_PENGGUNA", peran);
                                    }
                                } else if (getArguments() != null && getArguments().containsKey("NAMA_LENGKAP")) {
                                    intent.putExtras(getArguments());
                                } else {
                                    intent.putExtra("NAMA_LENGKAP", credential);
                                    boolean isEmail = credential.contains("@");
                                    if (isEmail) intent.putExtra("EMAIL", credential);
                                }

                                editor.apply();

                                Toast.makeText(getContext(), "Login Berhasil!", Toast.LENGTH_SHORT).show();

                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                requireActivity().finish();
                            } else {
                                String message = jsonObject.optString("message", "Login failed");
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("credential", credential);
                params.put("password", password);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void setupRegisterLink(TextView tvRegister) {
        String text = "Belum punya akun? Daftar";
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (getActivity() instanceof AuthActivity) {
                    ((AuthActivity) getActivity()).navigateToRegister();
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

        int startIndex = text.indexOf("Daftar");
        if (startIndex != -1) {
            ss.setSpan(clickableSpan, startIndex, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvRegister.setText(ss);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
    }
}