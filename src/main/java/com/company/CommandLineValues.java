package com.company;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class CommandLineValues {
    @Option(name = "--ab", aliases = { "--accesslog=" }, required = true,
            usage = "specify path for access log file")
    private String accessLogFile;


    public String getAccessLogFile() {
        return accessLogFile;
    }

    public void setAccessLogFile(String accessLogFile) {
        this.accessLogFile = accessLogFile;
    }

    public CommandLineValues(String... args){
        CmdLineParser parser = new CmdLineParser(this);
        parser.setUsageWidth(80);
        try{
            parser.parseArgument(args);
        }
        catch (CmdLineException e){
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }


    }


}
