package services;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseH2Services {

    private final static String JDBC_DRIVER = "org.h2.Driver";
    private  final static String DB_URL = "jdbc:h2:tcp://localhost/~/pract3";
    private final static String user = "sa";
    private final static String password = "";
    private static Connection dbConnection = null;

    private static Server server;


    public static Connection getDataBaseConnection(){

        try {
            Class.forName(JDBC_DRIVER);
            dbConnection = DriverManager.getConnection(DB_URL, user, password);
            System.out.println("[success] Connected to the database");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("[failed] An error occurred. Database Connection Failed!");
            e.printStackTrace();
        }
        return  dbConnection;
    }

    public static void startDb() {
        try {
            server = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists").start();
            System.out.println("[-] START DATABASE OK!");

            String status = Server.createWebServer("-trace", "-webPort", "9090").start().getStatus();
            System.out.println("Status Web: "+status);
        } catch (SQLException te) {
            System.out.println("Problem with DataBase: "+te.getMessage());
        }


    }


    public static void stopDb(){
        server.shutdown();
    }


}
