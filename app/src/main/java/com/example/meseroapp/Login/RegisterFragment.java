package com.example.meseroapp.Login;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.meseroapp.R;

import java.util.Random;

import data.dao.BarDAO;
import data.database.AppDatabase;
import data.entity.Bar;
import data.entity.User;
import data.service.EmailSenderService;
import data.service.UserService;

public class RegisterFragment extends Fragment {

    private EditText etFullName, etEmail, etPassword, etBar;
    private Spinner spinnerOpciones;
    private Button btnRegister;
    private TextView tvLogin, tvBossRegister;

    private UserService userService;
    private BarDAO barDAO;
    private EmailSenderService emailSenderService;

    public RegisterFragment() {}

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
        barDAO = db.barDao();

        etFullName = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        etBar = view.findViewById(R.id.etBar);
        spinnerOpciones = view.findViewById(R.id.spinnerOpciones);
        etPassword = view.findViewById(R.id.etPassword);
        btnRegister = view.findViewById(R.id.btnRegister);
        tvLogin = view.findViewById(R.id.tvLogin);
        tvBossRegister = view.findViewById(R.id.tvBossRegister);

        // Spinner
        String[] opciones = {"camarero", "cocina", "gerente"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(R.layout.open_spinner);
        spinnerOpciones.setAdapter(adapter);

        tvLogin.setOnClickListener(v -> {
            LoginFragment loginFragment = new LoginFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, loginFragment)
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

        // REGISTRO DE USUARIO
        btnRegister.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                            1001);
                    return; // salir hasta que el usuario acepte
                }
            }

            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String role = spinnerOpciones.getSelectedItem().toString();
            String barText = etBar.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || barText.isEmpty()) {
                Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int barId;
            try {
                barId = Integer.parseInt(barText);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "ID de bar inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {

                Bar bar = barDAO.getById(barId);
                if (bar == null) {
                    // Volvemos al UI para mostrar mensaje
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "No existe un bar con ese ID", Toast.LENGTH_SHORT).show()
                    );
                    return; // salimos del hilo secundario
                }

                String barEmail = bar.getEmail();
                int token = new Random().nextInt(9000) + 1000;

                // Enviar email también fuera del UI
                emailSenderService.sendUserVerifyEmail(barEmail, token, fullName);

                // Volvemos a UI para mostrar diálogo
                requireActivity().runOnUiThread(() -> {

                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Aprobación de bar");
                    builder.setMessage("Introduce el token enviado al correo del bar");

                    final EditText input = new EditText(requireContext());
                    input.setHint("Token aquí...");
                    builder.setView(input);

                    builder.setPositiveButton("Verificar", null);
                    builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

                    AlertDialog dialog = builder.create();

                    dialog.setOnShowListener(d1 -> {
                        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(v1 -> {

                            String tokenIngresado = input.getText().toString().trim();

                            if (tokenIngresado.equals(String.valueOf(token))) {
                                dialog.dismiss();

                                User user = new User();
                                user.setName(fullName);
                                user.setEmail(email);
                                user.setPassword(password);
                                user.setRol(role);
                                user.setBarId(barId);

                                new Thread(() -> {
                                    boolean success = userService.register(user);

                                    requireActivity().runOnUiThread(() -> {

                                        if (success) {
                                            LoginFragment loginFragment = new LoginFragment();
                                            getParentFragmentManager().beginTransaction()
                                                    .replace(R.id.fragment_container, loginFragment)
                                                    .addToBackStack(null)
                                                    .commit();
                                        } else {
                                            Toast.makeText(requireContext(),
                                                    "El email ya está en uso",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    });

                                }).start();

                            } else {
                                input.setError("Token incorrecto");
                            }
                        });
                    });

                    dialog.show();

                });

            }).start();
        });}
}