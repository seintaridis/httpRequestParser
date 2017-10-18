package com.ef.Parser.database;

import com.ef.Parser.HttpRequest;

import java.sql.*;
import java.util.ArrayList;

public class MysqlAccess {

    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/http_requests";
    private   String DB_USER = "";
    private   String DB_PASSWORD = "";


    public void configDB(String user,String pass){
        this.DB_USER=user;
        this.DB_PASSWORD=pass;
    }


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


    public ArrayList<String> getBlockedIps(Timestamp date1, String duration, String threshold) throws SQLException {

        Timestamp date2 = new Timestamp(System.currentTimeMillis());
        if (duration.equals("hourly")) {
            date2.setTime(date1.getTime() + 60 * 60 * 1000);
        } else if (duration.equals("daily")) {
            date2.setTime(date1.getTime() + 24 * 60 * 60 * 1000);
        }
        int thresholdInt = Integer.valueOf(threshold);


        String selectHttpRequestTableSQL = "SELECT ip FROM http_request  WHERE (req_date >= ? AND req_date < ?) GROUP BY ip HAVING count(*) > ?";


        Connection dbConnection = null;

        dbConnection = getDBConnection();
        PreparedStatement preparedStatement = dbConnection.prepareStatement(selectHttpRequestTableSQL);
        preparedStatement.setTimestamp(1, date1);
        preparedStatement.setTimestamp(2, date2);
        preparedStatement.setInt(3, thresholdInt);

        ResultSet rs = preparedStatement.executeQuery();
        ArrayList<String> ipList = new ArrayList<String>();
        while (rs.next()) {
            String ip = rs.getString("ip");
            ipList.add(ip);

        }
        return ipList;
    }


    public void insertRequestLogs(ArrayList<String> ips,String duration,String threashold){
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO log_block_request"
                + "(ip,duration,threashold) VALUES"
                + "(?,?,?)";


        try {
            dbConnection = getDBConnection();
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);

            int i = 0;
            for (String ip : ips) {
                // preparedStatement.setInt(1, 11);
                preparedStatement.setString(1,ip);
                preparedStatement.setString(2,duration);
                preparedStatement.setString(3,threashold);


                preparedStatement.addBatch();//83073 before  73833

                if (i % 1000 == 0 || i == ips.size()) {
                    preparedStatement.executeBatch(); // Execute every 1000 items.
                }
                //preparedStatement.executeUpdate();

            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }


    public void insertRequestsToTable(ArrayList<HttpRequest> httpRequests) throws SQLException {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO http_request"
                + "(ip,request,status,user_agent,req_date) VALUES"
                + "(?,?,?,?,?)";

        try {
            dbConnection = getDBConnection();
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);

            int i = 0;
            for (HttpRequest httpRequest : httpRequests) {
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
                if (i % 1000 == 0 || i == httpRequests.size()) {
                    preparedStatement.executeBatch(); // Execute every 1000 items.
                }
                //preparedStatement.executeUpdate();

            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }


    }
}
