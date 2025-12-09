package com.example.meseroapp.Main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.meseroapp.Main.Boss.BossFragment;
import com.example.meseroapp.R;
import com.example.meseroapp.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String role = SessionManager.getInstance(this).getUserRole();

        switch (role) {
            case "camarero":
                loadFragment(new CamareroFragment());
                break;

            case "cocina":
                loadFragment(new CocinaFragment());
                break;

            case "gerente":
                loadFragment(new BossFragment());
                break;

            default:
                loadFragment(new CamareroFragment());
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_containerMain, fragment)
                .commit();
    }
}