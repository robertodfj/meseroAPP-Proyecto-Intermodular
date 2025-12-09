package data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import data.entity.User;

@Dao
public interface UserDAO {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM User WHERE id = :id")
    User getById(int id);

    @Query("SELECT * FROM User WHERE email = :email")
    User getByEmail(String email);

    @Query("UPDATE User SET email = :email WHERE id = :userId")
    void updateEmail(int userId, String email);

    @Query("UPDATE User SET name = :nombre WHERE id = :userId")
    void updateNombre(int userId, String nombre);

    @Query("SELECT * FROM User WHERE barId = :barId")
    LiveData<List<User>> getEmpleados(int barId);

    @Query("SELECT * FROM User WHERE barId = :barId AND rol = 'camarero'")
    LiveData<List<User>> getCamareros(int barId);

    @Query("SELECT * FROM User WHERE barId = :barId AND rol = 'cocina'")
    LiveData<List<User>> getCocineros(int barId);
}