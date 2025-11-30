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
}
