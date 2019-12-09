//信管182 徐学印 201802104067
package com.example.demo.Service;

import com.example.demo.dao.UserDao;
import com.example.demo.domain.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
    private UserDao userDao = UserDao.getInstance();
    private static UserService userService = new UserService();

    public UserService() {
    }

    public static UserService getInstance(){
        return UserService.userService;
    }

    public Collection<User> getUsers() throws SQLException {
        return userDao.findAll();
    }

    public User getUser(Integer id) throws SQLException {
        return userDao.find(id);
    }

    public boolean add(User user, Connection connection) throws SQLException {
        return userDao.add(user,connection);
    }

    public boolean delete(Integer id,Connection connection) throws SQLException {
        return userDao.delete(id,connection);
    }

    public User login(String username, String password) throws SQLException {
        Collection<User> users = this.getUsers();
        User desiredUser = null;
        for(User user:users){
            if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
                desiredUser = user;
            }
        }
        return desiredUser;
    }

    public User findByUsername(String username) throws SQLException {
        return userDao.findByUsername(username);
    }
    public User findById(Integer id) throws SQLException {
        return userDao.find(id);
    }

    public boolean changePassword(Integer id, String password) throws SQLException {
        return userDao.changePassword(id,password);
    }
}

