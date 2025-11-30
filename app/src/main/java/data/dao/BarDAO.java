package data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import data.entity.Bar;

@Dao
public interface BarDAO {

    @Insert
    void insert(Bar bar);

    @Query("SELECT * FROM `Bar` WHERE id = :id")
    Bar getById(int id);

    @Query("SELECT * FROM Bar WHERE barName = :name")
    Bar getByName(String name);

    @Query("SELECT * FROM Bar WHERE email = :email")
    Bar getByEmail(String email);

    @Query("UPDATE `Bar` SET barName = :newBarName WHERE id = :id")
    void updateName(int id, String newBarName);

    @Query("UPDATE `Bar` SET email = :newEmail WHERE id = :id")
    void updateEmail(int id, String newEmail);

    @Delete
    void delete(Bar bar);

    public default boolean createBar(Bar bar) {
        Bar existBar = getByName(bar.getBarName());

        if (existBar == null) {
            insert(bar);
            System.out.println("Bar '" + bar.getBarName() + "' creado correctamente");
            return true;
        }

        System.out.println("Bar '" + bar.getBarName() + "' ya existe, prueba con otro nombre");
        return false;
    }

    public default boolean deleteBar(int id) {
        Bar bar = getById(id);

        if (bar != null) {
            delete(bar);
            System.out.println("Bar '" + bar.getBarName() + "' borrado correctamente");
            return true;
        }

        System.out.println("Bar no encontrado");
        return false;
    }

    public default boolean editBar(int id, String newName, String newEmail) {
        Bar bar = getById(id);

        if (bar == null) {
            System.out.println("El bar no existe");
            return false;
        }

        // Comprobaciones
        if (newName != null) {
            Bar existBar = getByName(newName);
            if (existBar != null && existBar.getId() != id) {
                System.out.println("El nuevo nombre ya está en uso");
                return false;
            }
        }

        if (newEmail != null) {
            Bar existEmail = getByEmail(newEmail);
            if (existEmail != null && existEmail.getId() != id) {
                System.out.println("El nuevo email ya está en uso");
                return false;
            }
        }

        // Actualizar
        if (newName != null) updateName(id, newName);
        if (newEmail != null) updateEmail(id, newEmail);

        System.out.println("Bar '" + id + "' editado correctamente");
        return true;
    }
}