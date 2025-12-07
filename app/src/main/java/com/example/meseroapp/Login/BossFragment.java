package com.example.meseroapp.Login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.meseroapp.R;

import data.database.AppDatabase;
import data.entity.Bar;
import data.service.BarService;

public class BossFragment extends Fragment {

    private EditText etEmail, etBarName;
    private Button btnCreate;
    private BarService barService;

    public BossFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_boss, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppDatabase db = AppDatabase.getInstance(requireContext());
        barService = new BarService(db.barDao());  // <-- CORREGIDO

        etEmail = view.findViewById(R.id.etEmail);
        etBarName = view.findViewById(R.id.etBarName);
        btnCreate = view.findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String barName = etBarName.getText().toString().trim();

            Bar bar = new Bar();
            bar.setBarName(barName);
            bar.setEmail(email);

            int token = (int) (Math.random() * 9000) + 1000;
            bar.setToken(token);

            if (barService.createBar(bar)) {

                // AlertDialog para ingresar el token
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Bar creado");
                builder.setMessage("Introduce el token que te hemos enviado al correo:");

                final EditText input = new EditText(requireContext());
                input.setHint("Token");

                builder.setView(input);

                builder.setPositiveButton("Verificar", null); // lo sobrescribimos luego
                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(d -> {
                    Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setOnClickListener(v1 -> {
                        String tokenIngresado = input.getText().toString().trim();

                        if (tokenIngresado.equals(String.valueOf(token))) {
                            dialog.dismiss();

                            // Ir a register fragment para registrar al gerente
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(requireContext());
                            builder.setTitle("ATENCIÓN");
                            builder.setMessage("Apunta el CODIGO BAR para que los empleados puedan registrarse:");

                            final TextView textView = new TextView(requireContext());
                            textView.setText("CODIGO BAR: " + bar.g());

                            builder.setView(input);

                            builder.setPositiveButton("Verificar", null); // lo sobrescribimos luego
                            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

                            AlertDialog dialog = builder.create();

                        } else {
                            input.setError("Token incorrecto");
                        }
                    });
                });

                dialog.show();
            }

        });
    }
}