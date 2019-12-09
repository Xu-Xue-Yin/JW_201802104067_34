//信管182 徐学印 201802104067
package com.example.demo.dao;

import com.example.demo.domain.Degree;
import com.example.demo.util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;


public final class DegreeDao {
	private static DegreeDao degreeDao = new DegreeDao();
	private DegreeDao(){}
	public static DegreeDao getInstance(){
		return degreeDao;
	}
	private static Collection<Degree> degrees;

	public Collection<Degree> findAll()throws SQLException{
		degrees = new HashSet<Degree>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from Degree");
		//从数据库中取出数据
		while (resultSet.next()){
			degrees.add(new Degree(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks")));
		}
		JdbcHelper.close(stmt,connection);
		return degrees;
	}

	public Degree find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement =
				connection.prepareStatement(
						"SELECT * FROM degree where id = ?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		Degree degree = null;
		if (resultSet.next()) {
			degree = new Degree(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"));
		}
		return degree;
	}

	public boolean update(Degree degree) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement =
				connection.prepareStatement(
						"UPDATE degree set description = ? where id = ?");
		preparedStatement.setString(1,degree.getDescription());
		preparedStatement.setInt(2,degree.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("修改了"+ affectedRowNum +"行数据");
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean add(Degree degree) throws SQLException {
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement =
				connection.prepareStatement(
						"INSERT INTO degree "+
								"(no,description,remarks)" +
								" VALUES (?,?,?)");
		preparedStatement.setString(1,degree.getNo());
		preparedStatement.setString(2,degree.getDescription());
		preparedStatement.setString(3,degree.getRemarks());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("添加了"+ affectedRowNum +"行数据");
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean delete(Integer id) throws SQLException {
		Degree degree = this.find(id);
		return this.delete(degree);
	}

	public boolean delete(Degree degree)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement =
				connection.prepareStatement(
						"Delete from degree WHERE id =?");
		preparedStatement.setInt(1,degree.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("删除了"+ affectedRowNum +"行数据");
		return affectedRowNum>0;
	}
}

