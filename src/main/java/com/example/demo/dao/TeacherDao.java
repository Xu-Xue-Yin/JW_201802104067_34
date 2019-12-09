//信管182 徐学印 201802104067
package com.example.demo.dao;

import com.example.demo.Service.DegreeService;
import com.example.demo.Service.DepartmentService;
import com.example.demo.Service.ProfTitleService;
import com.example.demo.Service.UserService;
import com.example.demo.domain.Teacher;
import com.example.demo.domain.User;
import com.example.demo.util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public final class TeacherDao {
    private static TeacherDao teacherDao = new TeacherDao();
    private TeacherDao() {}
    public static TeacherDao getInstance() {
        return teacherDao;
    }
    private static Collection<Teacher> teachers;

    public Collection<Teacher> findAll() throws SQLException {
        teachers = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        ResultSet resultSet =
                statement.executeQuery(
                        "select * from teacher");
        while (resultSet.next()) {
            teachers.add(new Teacher(resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getString("name"),
                    ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id")),
                    DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
                    DepartmentDao.getInstance().find(resultSet.getInt("department_id"))));
        }
        JdbcHelper.close(statement, connection);
        return teachers;
    }

    public Teacher find(Integer id) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement =
                connection.prepareStatement(
                        "SELECT * FROM teacher where id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Teacher teacher = null;
        if (resultSet.next()) {
            teacher = new Teacher((resultSet.getInt("id")),
                    resultSet.getString("no"),
                    resultSet.getString("name"),
                    ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id")),
                    DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
                    DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
        }
        return teacher;
    }

    public boolean update(Teacher teacher) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement =
                connection.prepareStatement(
                        "UPDATE teacher SET no=?,name=?,profTitle_id=?,degree_id=?,department_id=? where id = ?");
        preparedStatement.setString(1,teacher.getNo());
        preparedStatement.setString(2, teacher.getName());
        preparedStatement.setInt(3, teacher.getTitle().getId());
        preparedStatement.setInt(4, teacher.getDegree().getId());
        preparedStatement.setInt(5, teacher.getDepartment().getId());
        preparedStatement.setInt(6, teacher.getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("修改了" + affectedRowNum + "行数据");
        JdbcHelper.close(preparedStatement, connection);
        return affectedRowNum > 0;
    }

    public boolean add(Teacher teacher) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean affectedRowNum = false;
        try {
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            preparedStatement =
                    connection.prepareStatement(
                            "INSERT INTO teacher " +
                                    " (no,name, profTitle_id,degree_id,department_id) " +
                                    " VALUES (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,teacher.getNo());
            preparedStatement.setString(2, teacher.getName());
            preparedStatement.setInt(3, teacher.getTitle().getId());
            preparedStatement.setInt(4, teacher.getDegree().getId());
            preparedStatement.setInt(5, teacher.getDepartment().getId());
            affectedRowNum = preparedStatement.executeUpdate()>0;
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            teacher.setId(resultSet.getInt(1));
            java.util.Date date_util = new java.util.Date();
            Long date_long = date_util.getTime();
            Date date_sql = new Date(date_long);
            UserService.getInstance().add(new User(teacher.getNo(), teacher.getNo(), date_sql, teacher),connection);
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n errorCode=" + e.getErrorCode());
            try {
                //回滚当前连接所做的操作
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        } finally {
            try {
                //恢复自动提交
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRowNum;
    }

    public boolean delete(Teacher teacher) throws  SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean affectedRowNum = false;
        try {
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            UserService.getInstance().delete(teacher.getId(),connection);
            //在该连接上创建预编译语句对象
            preparedStatement = connection.prepareStatement("DELETE FROM Teacher WHERE id=?");
            //为预编译参数赋值
            preparedStatement.setInt(1, teacher.getId());
            affectedRowNum = preparedStatement.executeUpdate()>0;
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n errorcode=" + e.getErrorCode());
            try {
                //回滚当前连接所做的操作
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                //恢复自动提交
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //关闭连接
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum;
    }
    public Teacher findByNo(String no) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement =
                connection.prepareStatement(
                        "SELECT * FROM Teacher WHERE no=?");
        preparedStatement.setString(1,no);
        ResultSet resultSet = preparedStatement.executeQuery();
        Teacher teacher = null;
        if (resultSet.next()){
            teacher = new Teacher(resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getString("name"),
                    ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id")),
                    DegreeService.getInstance().find(resultSet.getInt("degree_id")),
                    DepartmentService.getInstance().find(resultSet.getInt("department_id"))
            );
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return teacher;
    }
}
