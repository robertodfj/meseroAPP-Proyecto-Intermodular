package data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import data.entity.Bar;
import data.entity.BarWithUsers;

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

    @Transaction
    @Query("SELECT * FROM Bar WHERE id = :id")
    BarWithUsers getBarWithUsers(int id);

    @Delete
    void delete(Bar bar);
}