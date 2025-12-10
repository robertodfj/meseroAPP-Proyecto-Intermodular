package com.example.meseroapp.Main.Boss;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meseroapp.R;
import com.example.meseroapp.utils.SessionManager;

import data.database.AppDatabase;
import data.entity.User;

public class UsersFragment extends Fragment {

    public UsersFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recycler = view.findViewById(R.id.rvEmpleados);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        UserAdapter adapter = new UserAdapter();
        recycler.setAdapter(adapter);

        int barId = SessionManager.getInstance(getContext()).getBarId();
        AppDatabase db = AppDatabase.getInstance(getContext());

        // Observamos los empleados del bar y actualizamos el RecyclerView
        db.userDao().getEmpleados(barId).observe(getViewLifecycleOwner(), adapter::setUsers);

        // Listener para editar usuario
        adapter.setOnEditClickListener(user -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Editar usuario");

            LinearLayout layout = new LinearLayout(requireContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 10);

            // EditText para nombre completo
            EditText etName = new EditText(requireContext());
            etName.setHint("Nombre completo");
            etName.setText(user.getName());
            layout.addView(etName);

            // EditText para email
            EditText etEmail = new EditText(requireContext());
            etEmail.setHint("Email");
            etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            etEmail.setText(user.getEmail());
            layout.addView(etEmail);

            // Spinner para rol
            Spinner spRole = new Spinner(requireContext());
            String[] roles = {"gerente", "cocina", "camarero"};
            ArrayAdapter<String> adapterRole = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    roles
            );
            adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRole.setAdapter(adapterRole);

            // Seleccionar el rol actual
            int spinnerPosition = adapterRole.getPosition(user.getRol().toLowerCase());
            spRole.setSelection(spinnerPosition);
            layout.addView(spRole);

            builder.setView(layout);

            // Botones del diálogo
            builder.setPositiveButton("Guardar", null);
            builder.setNegativeButton("Volver", (dialog, which) -> dialog.dismiss());
            builder.setNeutralButton("Borrar", null);

            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(d -> {
                Button btnGuardar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnBorrar = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

                // Guardar cambios
                btnGuardar.setOnClickListener(v -> {
                    String newName = etName.getText().toString().trim();
                    String newEmail = etEmail.getText().toString().trim();
                    String newRole = spRole.getSelectedItem().toString();

                    if (newName.isEmpty() || newEmail.isEmpty()) {
                        Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new Thread(() -> {
                        user.setName(newName);
                        user.setEmail(newEmail);
                        user.setRol(newRole);

                        db.userDao().update(user);  // Debes tener update(User user) en UserDAO

                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }).start();
                });

                // Borrar usuario
                btnBorrar.setOnClickListener(v -> {
                    new Thread(() -> {
                        db.userDao().(user);

                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "Usuario borrado", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }).start();
                });
            });

            dialog.show();
        });
    }
}