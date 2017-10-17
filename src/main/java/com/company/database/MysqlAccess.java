package com.company.database;

import com.company.HttpRequest;
import com.sun.tools.javac.jvm.ClassFile;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MysqlAccess {

    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/http_requests";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pao27jim";
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;

    String url = "jdbc:mysql://localhost:3306/dfd";
    String user = "root";
    String password = "pao27jim";


    public Connection getDBConnection() {

        Connection dbConnection = null;
        try {

            dbConnection = DriverManager.getConnection(
                    DB_CONNECTION, DB_USER, DB_PASSWORD);
            System.out.println("Connected succesfully..");
            return dbConnection;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return dbConnection;

    }






    public void insertRequestsToTable(ArrayList<HttpRequest> httpRequests) throws  SQLException{
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO http_request"
                + "(ip,request,status,user_agent,req_date) values"
                + "(?,?,?,?,?)";

        try {
            dbConnection=getDBConnection();
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);

            int i=0;
            for(HttpRequest httpRequest : httpRequests) {
               // preparedStatement.setInt(1, 11);
                preparedStatement.setString(1, httpRequest.getIp());
                preparedStatement.setString(2, httpRequest.getRequest());
                preparedStatement.setString(3, httpRequest.getStatus());
                preparedStatement.setString(4, httpRequest.getUserAgent());
                preparedStatement.setTimestamp(5, httpRequest.getDate());

                preparedStatement.addBatch();//83073 before  73833
                i++;
                // me 1000  71975    116484
                // me 10000  69605
                if (i % 100 == 0 || i == httpRequests.size()) {
                    preparedStatement.executeBatch(); // Execute every 1000 items.
                }
                //preparedStatement.executeUpdate();

            }


        }
        catch (SQLException e){
            System.out.println(e.getMessage());

        }


    }
}
