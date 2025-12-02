package data.service;

import data.dao.TableDAO;
import data.entity.Table;

public class TableService {

    private final TableDAO tableDao;

    public TableService(TableDAO tableDao) {
        this.tableDao = tableDao; // inyección de DAO
    }

    public boolean addTable(Table table) {
        Table existTable = tableDao.getByTableNumber(table.getTableNumber());
        if (existTable == null) {
            tableDao.insert(table);
            System.out.println("Mesa número " + table.getTableNumber() + " añadida con éxito");
            return true;
        }
        System.out.println("Ese número de mesa ya está en uso");
        return false;
    }

    public boolean deleteTable(int tableNumber) {
        Table table = tableDao.getByTableNumber(tableNumber);
        if (table != null) {
            tableDao.delete(table);
            System.out.println("Mesa número " + tableNumber + " eliminada con éxito");
            return true;
        }
        System.out.println("Fallo al eliminar la mesa " + tableNumber + ", comprueba que exista");
        return false;
    }

    public boolean editTable(int tableNumber, int newTableNumber) {
        Table existTable = tableDao.getByTableNumber(tableNumber);
        if (existTable != null) {
            tableDao.updateTableNumber(tableNumber, newTableNumber);
            System.out.println("Mesa número " + tableNumber + " renombrada a " + newTableNumber + " con éxito");
            return true;
        }
        System.out.println("La mesa número " + tableNumber + " no existe");
        return false;
    }
}