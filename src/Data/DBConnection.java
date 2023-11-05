/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Lenovo
 */
public class DBConnection {
   
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=CHAT";
    private static final String USERNAME = "toi";
    private static final String PASSWORD = "1";

    //Mở kết nối
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Đã kết nối tới database !");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi kết nối database !");
        }
        return connection;
    }
    
    //Đóng kết nối
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Đóng kết nối database !");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Lỗi khi đóng kết nối database !!");
            }
        }
    }
}
