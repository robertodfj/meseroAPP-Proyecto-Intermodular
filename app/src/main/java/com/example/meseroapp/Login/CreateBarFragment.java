package com.example.meseroapp.Login;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meseroapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executors;

import data.database.AppDatabase;
import data.entity.Bar;
import data.service.BarService;
import data.service.EmailSenderService;

public class CreateBarFragment extends Fragment {

    private TextInputEditText etEmail, etBarName;
    private MaterialButton btnCreate;
    private BarService barService;
    private EmailSenderService emailSenderService;


    public CreateBarFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppDatabase db = AppDatabase.getInstance(requireContext());
        barService = new BarService(db.barDao());

        emailSenderService = new EmailSenderService(
                db.orderDao(),
                db.productDao(),
                db.lineOrderDao(),
                db.barDao()
        );

        etEmail = view.findViewById(R.id.etEmail);
        etBarName = view.findViewById(R.id.etBarName);
        btnCreate = view.findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String barName = etBarName.getText().toString().trim();

            if (barName.isEmpty() || email.isEmpty()) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Error")
                        .setMessage("Complete todos los datos.")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                // Comprobar duplicados antes de cualquier acción
                boolean existsByName = barService.existBarByBarName(barName);
                boolean existsByEmail = barService.existsBarByEmail(email);

                if (existsByName || existsByEmail) {
                    requireActivity().runOnUiThread(() -> {
                        String msg;
                        if (existsByName && existsByEmail) msg = "El bar y el email ya existen.";
                        else if (existsByName) msg = "El bar ya existe, prueba con otro nombre.";
                        else msg = "El email ya está registrado, prueba con otro.";

                        new AlertDialog.Builder(requireContext())
                                .setTitle("Error")
                                .setMessage(msg)
                                .setPositiveButton("OK", null)
                                .show();
                    });
                    return; // Detener flujo
                }

                // Generar token
                int token = (int) (Math.random() * 9000) + 1000;

                // Enviar email
                boolean emailSent = emailSenderService.sendBarVerifyEmail(email, token);

                if (!emailSent) {
                    requireActivity().runOnUiThread(() -> new AlertDialog.Builder(requireContext())
                            .setTitle("Error")
                            .setMessage("No se pudo enviar el email. Revisa tu conexión.")
                            .setPositiveButton("OK", null)
                            .show());
                    return;
                }

                // Mostrar diálogo para introducir token
                requireActivity().runOnUiThread(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Verificar email");
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

                                // **Crear el bar aquí, solo si el token es correcto**
                                Executors.newSingleThreadExecutor().execute(() -> {
                                    Bar bar = new Bar();
                                    bar.setBarName(barName);
                                    bar.setEmail(email);

                                    long barId = barService.createBar(bar);

                                    if (barId == -1) {
                                        requireActivity().runOnUiThread(() ->
                                                new AlertDialog.Builder(requireContext())
                                                        .setTitle("Error")
                                                        .setMessage("No se pudo crear el bar. Inténtalo de nuevo.")
                                                        .setPositiveButton("OK", null)
                                                        .show()
                                        );
                                        return;
                                    }

                                    int barcode = (int) barId;

                                    requireActivity().runOnUiThread(() -> {
                                        new AlertDialog.Builder(requireContext())
                                                .setTitle("ATENCIÓN")

                                                .setMessage("Apunta este CÓDIGO BAR. Los empleados lo necesitarán: " + barcode)
                                                .setPositiveButton("Apuntado!", (dialog2, which2) -> {
                                                    RegisterFragment registerFragment = new RegisterFragment();
                                                    getParentFragmentManager().beginTransaction()
                                                            .replace(R.id.fragment_container, registerFragment)
                                                            .addToBackStack(null)
                                                            .commit();
                                                })
                                                .setCancelable(false)
                                                .show();
                                    });
                                });

                            } else {
                                input.setError("Token incorrecto");
                            }
                        });
                    });

                    dialog.show();
                });
            });
        });
    }
}