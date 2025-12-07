package com.example.meseroapp.Login;

import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meseroapp.Main.MainActivity;
import com.example.meseroapp.R;

import data.database.AppDatabase;
import data.service.UserService;

public class LoginFragment extends Fragment {

    // Elementos del layout
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister, tvBossRegister;
    private UserService userService;

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

        AppDatabase db = AppDatabase.getInstance(requireContext());
        userService = new UserService(db.userDao());

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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (userService.login(email, password)){
                    // Login exitoso -> a la patalla principal
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                } else {
                    // Mostrar mensaje de error
                    Toast.makeText(requireContext(), "Email o contraseña incorrectos o usuario inexistente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}