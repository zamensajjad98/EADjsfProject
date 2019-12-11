package data;

import java.sql.*;

/**
 * Created by fawad.tariq on 9/26/2019.
 */
public class DbConnection {
    private String dbURL = "jdbc:mysql://localhost:3306/a3";
    private String username = "root";
    private String password = "";
    private Connection connection;
    public DbConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dbURL,username,password);
            if(connection!=null){
                System.out.println("Success");
            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertRecord(String name,String username,String email,String password, String phone){
        try {
            String sqlQuery = "INSERT INTO data( name, username, password, email, phone) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, phone);

            int noOfRowsInserted = preparedStatement.executeUpdate();
            if(noOfRowsInserted>0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean authenticatePerson(String username,String password){
        try {
            String sqlQuery = "Select username from data WHERE username=? and password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2, password);
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void updateRecord(int id,String firstName,String lastName,String email){
        try {
            String sqlQuery = "UPDATE student SET first_name=?,last_name=?,email=? WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,firstName);
            preparedStatement.setString(2,lastName);
            preparedStatement.setString(3,email);
            preparedStatement.setInt(4,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecord(int id){
        try {
            String sqlQuery = "DELETE from student WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getRecord(int id){
        try {
            String sqlQuery = "SELECT * FROM student where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1,id);
            ResultSet result = preparedStatement.executeQuery();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getRecords(){
        try {
            String sqlQuery = "SELECT * FROM student";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
