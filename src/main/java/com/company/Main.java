package com.company;

import com.company.database.MysqlAccess;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

//        CommandLineValues values = new CommandLineValues(args);
//        CmdLineParser parser = new CmdLineParser(values);
//
//        try {
//            parser.parseArgument(args);
//        } catch (CmdLineException e) {
//            System.exit(1);
//        }
//        System.out.println(values.getAccessLogFile());

        String accessFile =args[1];
        System.out.println(accessFile);
        String logFile = args[1].replace("--accesslog=", "");
        String startDate = args[2].replace("--startDate=","");
        String duration = args[3].replace("--duration=","");
        String threshold = args[4].replace("--threshold=","");
        System.out.println("Log file: "+logFile);
        System.out.println("Start date: "+startDate);
        System.out.println("Duration: "+duration);
        System.out.println("Threshold: "+threshold);
        System.exit(1);



        final long startTime = System.currentTimeMillis();
        System.out.println("Program start..");
        String fileName = args[0];
        HttpRequestParser httpRequestParser = new HttpRequestParser(fileName);
        MysqlAccess mysqlAccess = new MysqlAccess();
        ArrayList<HttpRequest> httpRequests;




        try {
            httpRequests=httpRequestParser.processLineByLine();
            final long beforeMySql = System.currentTimeMillis();
            System.out.println("Time before insert to database: " + (beforeMySql - startTime) );
            mysqlAccess.insertRequestsToTable(httpRequests);
            final long endTime = System.currentTimeMillis();
            System.out.println("Total execution time: " + (endTime - startTime) );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }






}
