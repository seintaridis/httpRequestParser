package com.ef.Parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class HttpRequestParser {


    private Path filePath;
    private  static Charset ENCODING = StandardCharsets.UTF_8;
    private Timestamp date;
    private String dateString;
    private String ip;
    private String request;
    private String status;
    private String userAgent;

    public HttpRequestParser(String fileName){
        System.out.println("Parse file "+fileName);
        this.filePath= Paths.get(fileName);

    }

    public ArrayList<HttpRequest> processLineByLine() throws IOException {
        int x=0;
        ArrayList<HttpRequest> httpRequests = new ArrayList<HttpRequest>();
        try (Scanner scanner =  new Scanner(filePath, ENCODING.name())){
            while (scanner.hasNextLine() && x<10){
            //    x++;
                httpRequests.add(processLine(scanner.nextLine()));

            }
            return  httpRequests;
        }
    }

    public HttpRequest processLine(String line){
        HttpRequest httpRequest=null;
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter(Pattern.quote("|"));
        if (scanner.hasNext()){

            date = Timestamp.valueOf(scanner.next());
            ip = scanner.next();
            request = scanner.next();
            status = scanner.next();
            userAgent = scanner.next();
            httpRequest= new HttpRequest(date,ip,request,status,userAgent);
//            System.out.println("DETAILS FOR REQUEST");
//            System.out.println("Date is: " + httpRequest.getDate());
//            System.out.println("Ip is: "+ httpRequest.getIp());
//            System.out.println("Request is: "+ httpRequest.getRequest());
//            System.out.println("Status is: "+ httpRequest.getStatus());
//            System.out.println("User agent is " + httpRequest.getUserAgent());



        }
        return httpRequest;

    }



}
