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

    private UserAdapter adapter;
    private AppDatabase db;
    private int barId;

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

        adapter = new UserAdapter();
        recycler.setAdapter(adapter);

        barId = SessionManager.getInstance(getContext()).getBarId();
        db = AppDatabase.getInstance(getContext());

        // Mostrar todos los empleados al inicio
        db.userDao().getEmpleados(barId).observe(getViewLifecycleOwner(), adapter::setUsers);

        // Botón Filtrar
        Button btnFiltrar = view.findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(v -> mostrarDialogoFiltrado());

        // Listener para editar usuario
        adapter.setOnEditClickListener(this::mostrarDialogoEdicion);
    }

    private void mostrarDialogoFiltrado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Filtrar usuarios");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        // Spinner para rol
        Spinner spRole = new Spinner(requireContext());
        String[] roles = {"Todos", "gerente", "cocina", "camarero"};
        ArrayAdapter<String> adapterRole = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                roles
        );
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(adapterRole);
        layout.addView(spRole);

        // Spinner para estado activo
        Spinner spActive = new Spinner(requireContext());
        String[] estados = {"Todos", "Activo", "Inactivo"};
        ArrayAdapter<String> adapterActive = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                estados
        );
        adapterActive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spActive.setAdapter(adapterActive);
        layout.addView(spActive);

        builder.setView(layout);

        builder.setPositiveButton("Filtrar", (dialog, which) -> {
            String rolSeleccionado = spRole.getSelectedItem().toString();
            String estadoSeleccionado = spActive.getSelectedItem().toString();

            String rol = rolSeleccionado.equals("Todos") ? null : rolSeleccionado;
            Boolean isActive = null;
            if (estadoSeleccionado.equals("Activo")) isActive = true;
            else if (estadoSeleccionado.equals("Inactivo")) isActive = false;

            db.userDao().getEmpleadosFiltrados(barId, rol, isActive)
                    .observe(getViewLifecycleOwner(), adapter::setUsers);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void mostrarDialogoEdicion(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Editar usuario");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        EditText etName = new EditText(requireContext());
        etName.setHint("Nombre completo");
        etName.setText(user.getName());
        layout.addView(etName);

        EditText etEmail = new EditText(requireContext());
        etEmail.setHint("Email");
        etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etEmail.setText(user.getEmail());
        layout.addView(etEmail);

        Spinner spRole = new Spinner(requireContext());
        String[] roles = {"gerente", "cocina", "camarero"};
        ArrayAdapter<String> adapterRole = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                roles
        );
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(adapterRole);
        int spinnerPosition = adapterRole.getPosition(user.getRol().toLowerCase());
        spRole.setSelection(spinnerPosition);
        layout.addView(spRole);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", null);
        builder.setNegativeButton("Volver", (dialog, which) -> dialog.dismiss());
        builder.setNeutralButton("Borrar", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            Button btnGuardar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button btnBorrar = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

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

                    db.userDao().update(user);

                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
                }).start();
            });

            btnBorrar.setOnClickListener(v -> {
                new Thread(() -> {
                    db.userDao().delete(user);

                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Usuario borrado", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
                }).start();
            });
        });

        dialog.show();
    }
}