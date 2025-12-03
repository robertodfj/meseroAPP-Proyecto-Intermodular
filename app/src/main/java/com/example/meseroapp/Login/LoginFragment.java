package com.example.meseroapp.Login;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.meseroapp.R;
import com.example.meseroapp.RegisterFragment;

public class LoginFragment extends Fragment {

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

        // Aquí conectarás botones, listeners y lógica del login.
        // Ejemplo:
        Button btn = view.findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, registerFragment) // fragment_container es el FrameLayout del Activity
                        .addToBackStack(null) // Permite volver al login con el botón atrás
                        .commit();
            }
        });
    }
}