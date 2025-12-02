package data.service;

import static java.nio.file.Files.delete;

import data.dao.BarDAO;
import data.entity.Bar;

public class BarService {
    public boolean createBar(Bar bar) {
        Bar existBar = BarDAO.getByName(bar.getBarName());

        if (existBar == null) {
            BarDAO.insert(bar);
            System.out.println("Bar '" + bar.getBarName() + "' creado correctamente");
            return true;
        }

        System.out.println("Bar '" + bar.getBarName() + "' ya existe, prueba con otro nombre");
        return false;
    }

    public boolean deleteBar(int id) {
        Bar bar = BarDAO.getById(id);

        if (bar != null) {
            BarDAO.delete(bar);
            System.out.println("Bar '" + bar.getBarName() + "' borrado correctamente");
            return true;
        }

        System.out.println("Bar no encontrado");
        return false;
    }

    public boolean editBar(int id, String newName, String newEmail) {
        Bar bar = BarDAO.getById(id);

        if (bar == null) {
            System.out.println("El bar no existe");
            return false;
        }

        // Comprobaciones
        if (newName != null) {
            Bar existBar = BarDAO.getByName(newName);
            if (existBar != null && existBar.getId() != id) {
                System.out.println("El nuevo nombre ya está en uso");
                return false;
            }
        }

        if (newEmail != null) {
            Bar existEmail = BarDAO.getByEmail(newEmail);
            if (existEmail != null && existEmail.getId() != id) {
                System.out.println("El nuevo email ya está en uso");
                return false;
            }
        }

        // Actualizar
        if (newName != null) BarDAO.updateName(id, newName);
        if (newEmail != null) BarDAO.updateEmail(id, newEmail);

        System.out.println("Bar '" + id + "' editado correctamente");
        return true;
    }
}
