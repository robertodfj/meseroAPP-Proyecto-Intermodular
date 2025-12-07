package com.example.meseroapp.Login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        barService = new BarService(db.barDao());

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

            System.out.printf("Token para %s: %d\n", email, token);
            // TODO: Enviar token por email
            System.out.println("token email:" + token); // Solo para pruebas TODO eliminar sout

            if (barService.createBar(bar)) {

                // PRIMER DIALOGO: Pedir token
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Bar creado");
                builder.setMessage("Introduce el token enviado al correo:");

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

                            // SEGUNDO DIALOGO: Mostrar ID del bar
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(requireContext());
                            builder2.setTitle("ATENCIÓN");
                            builder2.setMessage("Apunta este CÓDIGO BAR. Los empleados lo necesitarán para registrarse.");

                            TextView textView = new TextView(requireContext());
                            textView.setPadding(50, 30, 50, 30);
                            textView.setTextSize(20);
                            textView.setText("CÓDIGO BAR: " + bar.getId());

                            builder2.setView(textView);

                            builder2.setPositiveButton("Apuntado!", (dialog2, which2) -> {
                                // Apuntado el codigo -> volver al register
                                RegisterFragment registerFragment = new RegisterFragment();
                                getParentFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, registerFragment)
                                        .addToBackStack(null)
                                        .commit();
                            });

                            builder2.setCancelable(false);
                            builder2.show();

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