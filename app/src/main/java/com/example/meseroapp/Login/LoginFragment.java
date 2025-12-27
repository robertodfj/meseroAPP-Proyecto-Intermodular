package com.example.meseroapp.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meseroapp.Main.MainActivity;
import com.example.meseroapp.R;

import data.database.AppDatabase;
import data.entity.User;
import data.service.UserService;

import com.example.meseroapp.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {

    // Elementos del layout
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegister, tvBossRegister;
    private UserService userService;

    public LoginFragment() {}

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

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvRegister = view.findViewById(R.id.tvRegister);
        tvBossRegister = view.findViewById(R.id.tvBossRegister);

        tvRegister.setOnClickListener(v -> {
            RegisterFragment registerFragment = new RegisterFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, registerFragment)
                    .addToBackStack(null)
                    .commit();
        });

        tvBossRegister.setOnClickListener(v -> {
            CreateBarFragment createBarFragment = new CreateBarFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, createBarFragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            new Thread(() -> {
                User user = userService.loginAndGetUser(email, password);

                requireActivity().runOnUiThread(() -> {
                    if (user != null) {
                        SessionManager.getInstance(requireContext())
                                .saveSession(user.getId(), user.getBarId(), user.getRol());

                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    } else {
                        Toast.makeText(requireContext(),
                                "Email o contraseña incorrectos o usuario inexistente",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }
}