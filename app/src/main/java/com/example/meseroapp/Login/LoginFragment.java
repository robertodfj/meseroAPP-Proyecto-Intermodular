package com.example.meseroapp.Login;

import android.content.Intent;
import android.os.Bundle;
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

        // Inicializamos la base de datos y el servicio de usuario
        AppDatabase db = AppDatabase.getInstance(requireContext());
        userService = new UserService(db.userDao());

        // Conexión con los elementos del layout
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvRegister = view.findViewById(R.id.tvRegister);
        tvBossRegister = view.findViewById(R.id.tvBossRegister);

        // Listener para ir al registro normal
        tvRegister.setOnClickListener(v -> {
            RegisterFragment registerFragment = new RegisterFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, registerFragment) // frameLayout del Activity
                    .addToBackStack(null) // permite volver al login con el botón atrás
                    .commit();
        });

        // Listener para ir al registro de jefe
        tvBossRegister.setOnClickListener(v -> {
            CreateBarFragment createBarFragment = new CreateBarFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, createBarFragment) // frameLayout del Activity
                    .addToBackStack(null) // permite volver al login con el botón atrás
                    .commit();
        });

        // Listener para el botón de login
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Ejecutamos la consulta a la base de datos en un hilo secundario
            new Thread(() -> {
                boolean success = userService.login(email, password);

                // Volvemos al hilo principal para actualizar la UI
                requireActivity().runOnUiThread(() -> {
                    if (success) {
                        // Login exitoso -> ir a la pantalla principal
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    } else {
                        // Mostrar mensaje de error
                        Toast.makeText(requireContext(),
                                "Email o contraseña incorrectos o usuario inexistente",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }
}