package com.ef.Parser;

import com.ef.Parser.database.MysqlAccess;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        String logFile = args[0].replace("--accesslog=", "");
        String startDate = args[1].replace("--startDate=","");
        String duration = args[2].replace("--duration=","");
        String threshold = args[3].replace("--threshold=","");
        String dbUser=args[4].replace("--user=","");
        String dbPass=args[5].replace("--pass=","");

        final long startTime = System.currentTimeMillis();
        System.out.println("Program start..");
        HttpRequestParser httpRequestParser = new HttpRequestParser(logFile);
        MysqlAccess mysqlAccess = new MysqlAccess();
        mysqlAccess.configDB(dbUser,dbPass);
        ArrayList<HttpRequest> httpRequests;
        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
                "yyyy-MM-dd.hh:mm:ss");
        Date lFromDate1 = null;
        try {
            lFromDate1 = datetimeFormatter1.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Timestamp startDateTimestamp = new Timestamp(lFromDate1.getTime());
        try {
            httpRequests=httpRequestParser.processLineByLine();
            final long beforeMySql = System.currentTimeMillis();
            System.out.println("Time before insert to database: " + (beforeMySql - startTime) );
            mysqlAccess.insertRequestsToTable(httpRequests);
            ArrayList<String> blockedIps=mysqlAccess.getBlockedIps(startDateTimestamp,duration,threshold);
            System.out.println("Blocked ips "+ "Duration: "+duration +" Threashold "+threshold);
            for(String ip : blockedIps){
                System.out.println(ip);
            }
            mysqlAccess.insertRequestLogs(blockedIps,duration,threshold);
            final long endTime = System.currentTimeMillis();
            System.out.println("Total execution time: " + (endTime - startTime) );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }






}
