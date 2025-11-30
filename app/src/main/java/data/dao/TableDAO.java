package data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import data.entity.Table;

@Dao
public interface TableDAO {
    @Insert
    void insert(Table table);

    @Query("SELECT * FROM `Table` WHERE id = :id")
    Table getById(int id);

    @Query("SELECT * FROM `Table` WHERE tableNumber = :tableNumber")
    Table getByTableNumber(int tableNumber);

    @Delete
    void delete(Table table);

    @Query("UPDATE `Table` SET tableNumber = :newTableNumber WHERE tableNumber = :tableNumber")
    void updateTableNumber(int tableNumber, int newTableNumber);

    public default boolean addTable(Table table){
        Table existTable = getByTableNumber(table.getTableNumber());
        if (existTable == null) {
            insert(table);
            System.out.println("Mesa numero " +table.getTableNumber()+ " añadida con exito");
            return true;
        }
        System.out.println("Ese numero de mesa ya esta en uso");
        return false;
    }

    public default boolean deleteTable(int tableNumber){
        Table table = getByTableNumber(tableNumber);
        if (table != null){
            delete(table);
            System.out.println("Mesa numero " +tableNumber+ " eliminada con exito");
            return true;
        }
        System.out.println("Fallo al eliminar la mesa " +tableNumber+ " comprueba que esta existe");
        return false;
    }

    public default boolean editTable(int tableNumber, int newTableNumber){
        Table existTable = getByTableNumber(tableNumber);
        if (existTable != null){
            updateTableNumber(tableNumber, newTableNumber);
            System.out.println("Mesa numero: " +tableNumber+ " renombrada a " +newTableNumber+ " con exito");
            return true;
        }
        System.out.println("La mesa numero " +tableNumber+ " no existe");
        return false;
    }
}
