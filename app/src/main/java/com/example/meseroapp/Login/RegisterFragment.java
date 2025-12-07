package com.example.meseroapp.Login;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.meseroapp.R;

import java.util.Date;

import data.database.AppDatabase;
import data.entity.User;
import data.service.UserService;

public class RegisterFragment extends Fragment {

    private EditText etFullName, etEmail, etPassword, etBar;
    private Spinner spinnerOpciones;
    private Button btnRegister;
    private TextView tvLogin, tvBossRegister;
    private UserService userService;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppDatabase db = AppDatabase.getInstance(requireContext());
        userService = new UserService(db.userDao());

        etFullName = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        etBar = view.findViewById(R.id.etBar);
        spinnerOpciones = view.findViewById(R.id.spinnerOpciones);
        etPassword = view.findViewById(R.id.etPassword);
        btnRegister = view.findViewById(R.id.btnRegister);
        tvLogin = view.findViewById(R.id.tvLogin);
        tvBossRegister = view.findViewById(R.id.tvBossRegister);


        // Opciones spinner
        String[] opciones = {"camarero", "cocina", "gerente"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpciones.setAdapter(adapter);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, loginFragment) // frameLayout del Activity
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

        // Registar un usuario en la BD
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = etFullName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String role = spinnerOpciones.getSelectedItem().toString();
                int barID = Integer.parseInt(etBar.getText().toString().trim());

                // Crear usuario usando constructor vacío
                User user = new User();
                user.setName(fullName);
                user.setEmail(email);
                user.setPassword(password);
                user.setRol(role);
                user.setBarId(barID);

                // Registrar usuario
                if (userService.register(user)){
                    // Volver al login
                    LoginFragment loginFragment = new LoginFragment();
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, loginFragment)
                            .addToBackStack(null)
                            .commit();
                }else {
                    Toast.makeText(requireContext(), "El email ya esta en uso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}