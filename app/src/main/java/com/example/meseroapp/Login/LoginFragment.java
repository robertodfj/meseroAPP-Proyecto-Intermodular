package com.example.meseroapp.Login;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meseroapp.R;

public class LoginFragment extends Fragment {

    // Elementos del layout
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister, tvBossRegister;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Conexión con los elementos
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvRegister = view.findViewById(R.id.tvRegister);
        tvBossRegister = view.findViewById(R.id.tvBossRegister);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, registerFragment) // frameLayout del Activity
                        .addToBackStack(null) // permite volver al login con el botón atrás
                        .commit();
            }
        });

        tvBossRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BossFragment bossFragment = new BossFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, bossFragment) // frameLayout del Activity
                        .addToBackStack(null) // permite volver al login con el botón atrás
                        .commit();
            }
        });
    }
}