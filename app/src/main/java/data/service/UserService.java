package data.service;

import data.dao.UserDAO;
import data.entity.User;

public class UserService {

    private final UserDAO userDao;

    public UserService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public boolean register(User newUser) {
        try {
            userDao.insert(newUser);
            System.out.println("Usuario registrado correctamente");
            return true;
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            System.out.println("El email ya está en uso");
            return false;
        }
    }

    public User loginAndGetUser(String email, String password) {
        User user = userDao.getByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login exitoso");
            return user;
        } else {
            System.out.println("Email o contraseña incorrectos");
            return null;
        }
    }

    public boolean editUser(String email, String newName, String newEmail, String endDate) {
        User user = userDao.getByEmail(email);
        if (user != null) {
            if (newEmail != null) userDao.updateEmail(user.getId(), newEmail);
            if (newName != null) userDao.updateNombre(user.getId(), newName);
            System.out.println("Usuario editado correctamente");
            return true;
        } else {
            System.out.println("No se puede editar el usuario: no existe");
            return false;
        }
    }
}