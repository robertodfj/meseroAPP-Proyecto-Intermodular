package com.example.meseroapp.Main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meseroapp.R;
import com.example.meseroapp.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String role = SessionManager.getInstance(this).getUserRole();

        switch (role) {
            case "camarero":
                setContentView(R.layout.fragment_camarero));
                break;

            case "cocina":
                setContentView(R.layout.fragment_camarero));
                break;

            case "gerente":
                setContentView(R.layout.fragment_camarero));
                break;

            default:
                // Si no hay rol o es desconocido
                setContentView(R.layout.fragment_camarero);
                break;
        }
    }
}