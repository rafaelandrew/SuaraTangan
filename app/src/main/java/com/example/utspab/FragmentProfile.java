package com.example.utspab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class FragmentProfile extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvUsernameProfile = view.findViewById(R.id.tv_username_profile);
        Button btnInfoAkun = view.findViewById(R.id.btn_info_akun);
        Button btnSandi = view.findViewById(R.id.btn_ubah_sandi);
        Button btnBantuan = view.findViewById(R.id.btn_bantuan);
        Button btnKotakSaran = view.findViewById(R.id.btn_kotak_saran);
        Button btnKeluar = view.findViewById(R.id.btn_keluar);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("NAMA_LENGKAP")) {
            String namaLengkap = intent.getStringExtra("NAMA_LENGKAP");
            if (namaLengkap != null && !namaLengkap.isEmpty()) {
                tvUsernameProfile.setText(namaLengkap);
            }
        }

        btnInfoAkun.setOnClickListener(v -> {
            Intent infoIntent = new Intent(getActivity(), InfoAkunActivity.class);
            if (getActivity().getIntent().getExtras() != null) {
                infoIntent.putExtras(getActivity().getIntent().getExtras());
            }
            startActivity(infoIntent);
        });

        btnSandi.setOnClickListener(v -> startActivity(new Intent(getActivity(), UbahSandiActivity.class)));
        btnBantuan.setOnClickListener(v -> startActivity(new Intent(getActivity(), BantuanActivity.class)));
        btnKotakSaran.setOnClickListener(v -> startActivity(new Intent(getActivity(), KotakSaranActivity.class)));

        btnKeluar.setOnClickListener(v -> showLogoutOptions());

        return view;
    }

    private void showLogoutOptions() {
        if (getActivity() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_custom_logout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Button btnLogout = dialogView.findViewById(R.id.btn_dialog_logout);
        Button btnDelete = dialogView.findViewById(R.id.btn_dialog_delete);
        TextView tvCancel = dialogView.findViewById(R.id.tv_dialog_cancel);

        btnLogout.setOnClickListener(v -> {
            dialog.dismiss();
            performLogout(false);
        });

        btnDelete.setOnClickListener(v -> {
            dialog.dismiss();
            showFinalDeleteConfirmation();
        });

        tvCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showFinalDeleteConfirmation() {
        if (getActivity() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm_delete, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Button btnConfirmDelete = dialogView.findViewById(R.id.btn_confirm_delete);
        TextView tvCancelDelete = dialogView.findViewById(R.id.tv_cancel_delete);

        btnConfirmDelete.setOnClickListener(v -> {
            dialog.dismiss();
            performLogout(true);
        });

        tvCancelDelete.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void performLogout(boolean isDelete) {
        if (getActivity() == null) return;

        SharedPreferences prefs = getActivity().getSharedPreferences("GoScorePrefs", Context.MODE_PRIVATE);

        if (isDelete) {
            int userId = prefs.getInt("USER_ID", -1);

            if (userId == -1) {
                Toast.makeText(getContext(), "Error: User not found", Toast.LENGTH_SHORT).show();
                return;
            }

            showPasswordInputDialog(userId, prefs);
        } else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(getContext(), "Berhasil keluar", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), AuthActivity.class);
            intent.putExtra("SHOW_SIGN_IN", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void showPasswordInputDialog(int userId, SharedPreferences prefs) {
        if (getActivity() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_password_input, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        com.google.android.material.textfield.TextInputEditText etPassword = dialogView.findViewById(R.id.et_password_confirm);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm_password);
        TextView tvCancel = dialogView.findViewById(R.id.tv_cancel_password);

        btnConfirm.setOnClickListener(v -> {
            String password = etPassword.getText().toString().trim();

            if (password.isEmpty()) {
                Toast.makeText(getContext(), "Masukkan kata sandi Anda", Toast.LENGTH_SHORT).show();
                return;
            }

            dialog.dismiss();
            performDeleteAccount(userId, password, prefs);
        });

        tvCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void performDeleteAccount(int userId, String password, SharedPreferences prefs) {
        if (getActivity() == null) return;

        com.android.volley.RequestQueue queue = com.android.volley.toolbox.Volley.newRequestQueue(requireContext());
        com.android.volley.toolbox.StringRequest stringRequest = new com.android.volley.toolbox.StringRequest(
                com.android.volley.Request.Method.POST,
                ApiConfig.DELETE_ACCOUNT_URL,
                response -> {
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.optString("message", "");

                        if ("success".equals(status)) {
                            // Clear SharedPreferences
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.clear();
                            editor.apply();

                            Toast.makeText(getContext(), "Akun berhasil dihapus", Toast.LENGTH_SHORT).show();

                            // Go to sign up page
                            Intent intent = new Intent(getActivity(), AuthActivity.class);
                            intent.putExtra("SHOW_SIGN_UP", true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (org.json.JSONException e) {
                        Toast.makeText(getContext(), "Error deleting account", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("password", password);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}