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

    @Query("UPDATE User SET endDate = :fechaFin WHERE id = :userId")
    void updateFechaFin(int userId, String fechaFin);

    @Query("UPDATE User SET email = :email WHERE id = :userId")
    void updateEmail(int userId, String email);

    @Query("UPDATE User SET name = :nombre WHERE id = :userId")
    void updateNombre(int userId, String nombre);
}