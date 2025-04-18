package com.example.demo5;

import java.sql.*;
import java.util.ArrayList;

public class Database {

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        String login = "root";
        String password = "root";
        String conn = "jdbc:mysql://localhost:3306/user001_db1";
        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(conn, login, password);
    }

    public boolean Login(String login, String password) throws ClassNotFoundException, SQLException {
        String sql = "SELECT 1 FROM users WHERE login = '" + login + "' AND password = '" + password + "'";
        Statement stmt = getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        return rs.next();
    }

    public boolean getPassword(String password) throws ClassNotFoundException, SQLException {
        String sql = "SELECT password FROM users WHERE password = '" + password + "'";
        Statement stmt = getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        return rs.next();
    }
    public boolean checkLogin(String login, String password) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT login , password from users where login = ? and password = ?");
        statement.setString(1, login);
        statement.setString(2, password);
        return statement.executeQuery().next();
    }

    public void createPassword(String login, String password) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET password = ? WHERE login = ?");
        statement.setString(1, password);
        statement.setString(2, login);
        statement.executeUpdate();
    }
    public boolean checkFirstEnter(String login) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        boolean first_enter = false;
        PreparedStatement statement = connection.prepareStatement("SELECT first_enter from users where login = ?");
        statement.setString(1, login);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            first_enter = resultSet.getBoolean("first_enter");
        }
        System.out.println(first_enter);
        return first_enter;
    }
    public void updateFirstEnter(String login) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET first_enter = 1  where login = ?");
        statement.setString(1, login);
        statement.executeUpdate();
    }
    public int checkToBlock(String login) throws SQLException, ClassNotFoundException {
        int count=0;
        Connection connection = getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT failed_counts from users where login = ?");
        stmt.setString(1, login);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            count=resultSet.getInt("failed_counts");
        }
        return count;
    }
    public void countToBlock(String login) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        int count = checkToBlock(login);
        PreparedStatement statement = connection.prepareStatement("UPDATE users set failed_counts = ? where login = ?");
        statement.setInt(1, count+1);
        statement.setString(2, login);
        statement.executeUpdate();
    }
    public void cancelBlock(String login) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        int count = checkToBlock(login);
        PreparedStatement statement = connection.prepareStatement("UPDATE users set failed_counts = 0 where login = ?");
        statement.setString(1, login);
        statement.executeUpdate();
    }

    public ArrayList<Person> getUsers() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users";
        Statement stmt = getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<Person> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Person(rs.getString("login"),rs.getString("password"), rs.getInt("failed_counts")));
        }
        System.out.println(list);
        return list;
    }

    public void createUser(String login, String password) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (login, password) VALUES (?, ?)");
        statement.setString(1, login);
        statement.setString(2, password);
        statement.executeUpdate();
    }
    public void updateLogin(String login, String newLogin) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET login = ? WHERE login = ?");
        statement.setString(1, newLogin);
        statement.setString(2, login);
        statement.executeUpdate();

    }
    public void updatePassword(String password, String newPassword) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET password = ? WHERE password = ?");
        statement.setString(1, newPassword);
        statement.setString(2, password);
        statement.executeUpdate();

    }
    public void updateBlock(String login) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET failed_counts = 0 WHERE login = ?");
        statement.setString(1, login);
        statement.executeUpdate();
    }

}
