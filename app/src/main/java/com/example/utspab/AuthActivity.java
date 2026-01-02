package com.example.utspab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.getBooleanExtra("SHOW_SIGN_IN", false)) {
                    navigateToLogin(null);
                    return;
                } else if (intent.getBooleanExtra("SHOW_SIGN_UP", false)) {
                    navigateToRegister();
                    return;
                }
            }
            loadFragment(new AuthLandingFragment(), false);
        }
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );

        transaction.replace(R.id.auth_fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    public void navigateToLogin(Bundle bundle) {
        LoginFragment loginFragment = new LoginFragment();
        if (bundle != null) {
            loginFragment.setArguments(bundle);
        }
        loadFragment(loginFragment, true);
    }

    public void navigateToRegister() {
        loadFragment(new RegisterFragment(), true);
    }
}