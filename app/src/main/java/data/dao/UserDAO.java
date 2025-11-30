package data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import data.entity.User;

@Dao
public interface UserDAO {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM User WHERE id = :id")
    User getById(int id);

    @Query("SELECT * FROM User WHERE email = :email")
    User getByEmail(String email);

    // Actualizar la fecha de fin de un usuario por su id
    @Query("UPDATE User SET endDate = :fechaFin WHERE id = :userId")
    void updateFechaFin(int userId, String fechaFin);

    // Actualizar el email de un usuario por su id
    @Query("UPDATE User SET email = :email WHERE id = :userId")
    void updateEmail(int userId, String email);

    // Actualizar el nombre de un usuario por su id
    @Query("UPDATE User SET name = :nombre WHERE id = :userId")
    void updateNombre(int userId, String nombre);

    // Método por defecto para registro
    public default boolean register(User newUser) {
        String email = newUser.getEmail();
        User existUser = getByEmail(email);

        if (existUser != null) {
            System.out.println("El email ya está en uso");
            return false; // registro fallido
        }

        insert(newUser);
        return true; // registro exitoso
    }

    public default boolean login(String email, String password){
        User user = getByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return true; // login exitoso
        } else {
            System.out.println("Email o contraseña incorrectos");
            return false; // login fallido
        }
    }

    public default boolean editUser(String email, String newName, String newEmail, String endDate){
        User user = getByEmail(email);
        if (user != null) {
            if (endDate != null) updateFechaFin(user.getId(), endDate);
            if (newEmail != null) updateEmail(user.getId(), newEmail);
            if (newName != null) updateNombre(user.getId(), newName);
            return true;
        } else {
            System.out.println("No se puede editar el usuario: no existe");
            return false;
        }
    }
}
