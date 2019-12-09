//信管182 徐学印 201802104067
package com.example.demo.dao;


import com.example.demo.Service.TeacherService;
import com.example.demo.domain.User;
import com.example.demo.util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public final class UserDao {
    private static UserDao userDao=new UserDao();
    private UserDao(){}
    public static UserDao getInstance(){
        return userDao;
    }
    private static Collection<User> users;

    public Collection<User> findAll() throws SQLException {
        users = new HashSet<User>();
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from user");
        while(resultSet.next()){
            users.add(new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getDate("loginTime"),
                    TeacherService.getInstance().findByNo(resultSet.getString("username"))));
        }
        //关闭资源
        JdbcHelper.close(stmt,connection);
        return UserDao.users;
    }

    public User find(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement =
                connection.prepareStatement(
                        "SELECT * FROM user WHERE id=?");
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user = null;
        if (resultSet.next()){
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getDate("loginTime"),
                    TeacherService.getInstance().findByNo(resultSet.getString("username"))
            );
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
    }

    public boolean add(User user,Connection connection) throws SQLException{
        //在该链接上创建预编译语句对象
        PreparedStatement pstmt =
                connection.prepareStatement(
                        "INSERT INTO USER " +
                                " (username,password,loginTime,teacher_id) " +
                                " VALUES(?,?,?,?)");
        //为预编译参数赋值
        pstmt.setString(1,user.getUsername());
        pstmt.setString(2,user.getPassword());
        pstmt.setDate(3, (Date) user.getLoginTime());
        pstmt.setInt(4,user.getTeacher().getId());
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("添加了" + affectedRowNum + "行记录");
        //关闭对象
        pstmt.close();
        return affectedRowNum > 0;
    }

    public boolean delete(Integer id,Connection connection) throws SQLException{
        //在该链接上创建预编译语句对象
        PreparedStatement pstmt =
                connection.prepareStatement(
                        "delete from user where teacher_id=?");
        //为预编译参数赋值
        pstmt.setInt(1,id);
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了" + affectedRowNum + "行记录");
        pstmt.close();
        return affectedRowNum > 0;
    }

    public static void main(String[] args)throws SQLException{
        UserDao dao = new UserDao();
        Collection<User> users = dao.findAll();
        System.out.println(dao.login("username","password"));
    }

    private static void display(Collection<User> users) {
        for (User user : users) {
            System.out.println(user);
        }
    }

    public User findByUsername(String username) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement =
                connection.prepareStatement(
                        "SELECT * FROM USER WHERE USERNAME=?");
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getDate("loginTime"),
                    TeacherService.getInstance().findByNo(resultSet.getString("username")));
        return user;
    }

    public boolean changePassword(Integer id, String newPassword) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement pstmt =
                connection.prepareStatement(
                        "UPDATE user SET password=? WHERE username=?");
        pstmt.setString(1,newPassword);
        pstmt.setInt(2,id);
        int affectedRowNum = pstmt.executeUpdate();
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum>0;
    }

    public User login(String username, String password) throws SQLException {
        User user = userDao.findByUsername(username);
        if (username == null || user == null) {
            return null;
        } else if (user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }
}
