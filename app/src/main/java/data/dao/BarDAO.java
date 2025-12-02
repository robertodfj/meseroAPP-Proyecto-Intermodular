package data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import data.entity.Bar;
import data.entity.BarWithUsers;

@Dao
public interface BarDAO {

    @Insert
    static void insert(Bar bar);

    @Query("SELECT * FROM `Bar` WHERE id = :id")
    static Bar getById(int id);

    @Query("SELECT * FROM Bar WHERE barName = :name")
    static Bar getByName(String name);

    @Query("SELECT * FROM Bar WHERE email = :email")
    static Bar getByEmail(String email);

    @Query("UPDATE `Bar` SET barName = :newBarName WHERE id = :id")
    static void updateName(int id, String newBarName);

    @Query("UPDATE `Bar` SET email = :newEmail WHERE id = :id")
    static void updateEmail(int id, String newEmail);

    @Query("SELECT * FROM Bar WHERE id = :id")
    BarWithUsers getBarWithUsers(int id);

    @Delete
    static void delete(Bar bar);
}