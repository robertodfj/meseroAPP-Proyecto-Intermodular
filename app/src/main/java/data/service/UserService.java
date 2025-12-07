package data.service;

import data.dao.UserDAO;
import data.entity.User;

public class UserService {

    private final UserDAO userDao;

    public UserService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public boolean register(User newUser) {
        String email = newUser.getEmail();
        User existUser = userDao.getByEmail(email);

        if (existUser != null) {
            System.out.println("El email ya está en uso");
            return false;
        }

        userDao.insert(newUser);
        System.out.println("Usuario registrado correctamente");
        return true;
    }

    public boolean login(String email, String password) {
        User user = userDao.getByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login exitoso");
            return true;
        } else {
            System.out.println("Email o contraseña incorrectos");
            return false;
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