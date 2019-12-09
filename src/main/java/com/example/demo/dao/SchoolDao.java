//信管182 徐学印 201802104067
package com.example.demo.dao;

import com.example.demo.domain.School;
import com.example.demo.util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public final class SchoolDao {
	private static SchoolDao schoolDao = new SchoolDao();
	private static Collection<School> schools;
	public SchoolDao(){}
	public static SchoolDao getInstance(){
		return schoolDao;
	}
	public Collection<School> findAll()throws SQLException{
		schools = new HashSet<School>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from School");
		//从数据库中取出数据
		while (resultSet.next()){
			schools.add(new School(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks")));
		}
		JdbcHelper.close(stmt,connection);
		return schools;
	}

	public School find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement =
				connection.prepareStatement(
						"SELECT * FROM school where id = ?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		School school = null;
		if (resultSet.next()) {
			school = new School(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"));
		}
		return school;
	}

	public boolean update(School school) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement =
				connection.prepareStatement(
						"UPDATE school set description = ? where id = ?");
		preparedStatement.setString(1,school.getDescription());
		preparedStatement.setInt(2,school.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("修改了"+ affectedRowNum +"行数据");
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean add(School school) throws SQLException {
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement =
				connection.prepareStatement(
						"INSERT INTO school "+
								"(no,description,remarks)" +
								" VALUES (?,?,?)");
		preparedStatement.setString(1,school.getNo());
		preparedStatement.setString(2,school.getDescription());
		preparedStatement.setString(3,school.getRemarks());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("添加了"+ affectedRowNum +"行数据");
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean delete(Integer id) throws SQLException {
		School school = this.find(id);
		return this.delete(school);
	}

	public boolean delete(School school)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement =
				connection.prepareStatement(
						"Delete from school WHERE id =?");
		preparedStatement.setInt(1,school.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("删除了"+ affectedRowNum +"行数据");
		return affectedRowNum>0;
	}
}

