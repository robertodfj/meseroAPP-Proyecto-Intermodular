package data.service;

import static java.nio.file.Files.delete;

import data.dao.BarDAO;
import data.entity.Bar;

public class BarService {

    private final BarDAO barDao;

    public BarService(BarDAO barDao) {
        this.barDao = barDao;
    }

    public long createBar(Bar bar) {
        Bar existBar = barDao.getByName(bar.getBarName());

        if (existBar != null) {
            return -1; // Ya existe
        }

        return barDao.insert(bar);
    }

    public boolean deleteBar(int id) {
        Bar bar = barDao.getById(id);

        if (bar != null) {
            barDao.delete(bar);
            System.out.println("Bar '" + bar.getBarName() + "' borrado correctamente");
            return true;
        }

        System.out.println("Bar no encontrado");
        return false;
    }

    public boolean existsBarByEmail(String email) {
        Bar bar = barDao.getByEmail(email);
        return bar != null;
    }

    public boolean existBarByBarName(String barName) {
        Bar bar = barDao.getByEmail(barName);
        return bar != null;
    }

    public boolean editBar(int id, String newName, String newEmail) {
        Bar bar = barDao.getById(id);

        if (bar == null) {
            System.out.println("El bar no existe");
            return false;
        }

        if (newName != null) {
            Bar existBar = barDao.getByName(newName);
            if (existBar != null && existBar.getId() != id) {
                System.out.println("El nuevo nombre ya está en uso");
                return false;
            }
        }

        if (newEmail != null) {
            Bar existEmail = barDao.getByEmail(newEmail);
            if (existEmail != null && existEmail.getId() != id) {
                System.out.println("El nuevo email ya está en uso");
                return false;
            }
        }

        if (newName != null) barDao.updateName(id, newName);
        if (newEmail != null) barDao.updateEmail(id, newEmail);

        System.out.println("Bar '" + id + "' editado correctamente");
        return true;
    }
}