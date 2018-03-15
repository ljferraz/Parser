package com.ef;

import com.ef.utils.ArgumentsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class Parser {

    private static final Logger log = LoggerFactory.getLogger(Parser.class);

    public static void main(String args[]) {
        if ((args.length == 0)
                || (Arrays.stream(args).anyMatch(s -> s.contains("usage")))
                || (!Arrays.stream(ArgumentsType.values()).allMatch(argName -> Arrays.stream(args).anyMatch(arg -> arg.split("=")[0].replaceAll("-", "").equals(argName.name()))))) {
            printUsage();
            return;
        }

        SpringApplication.run(Parser.class, args);
    }

    private static void printUsage() {
        System.out.println("The tool takes 'accesslog', 'startDate', 'duration' and 'threshold' as command line arguments. 'accesslog' is the log file absolute path, 'startDate' is of 'yyyy-MM-dd.HH:mm:ss' format, 'duration' can take only 'hourly', 'daily' as inputs and 'threshold' can be an integer.");
        System.out.println("java -jar parser-1.0.jar --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=daily --threshold=250");
        System.out.println();
        System.out.println("In the case that database connection parameters are not set under application.properties, you can add them on command line arguments. For example:");
        System.out.println("java -jar parser-1.0.jar --startDate=2017-01-01.00:00:11 --duration=hourly --threshold=100 --accesslog=/path/to/file --spring.datasource.url=jdbc:mysql://Database/parser?useTimezone=true&serverTimezone=UTC --spring.datasource.username=user --spring.datasource.password=password");
    }
}
