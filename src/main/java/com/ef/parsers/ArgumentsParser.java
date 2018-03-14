package com.ef.parsers;

import com.ef.utils.ArgumentsType;
import com.ef.utils.DurationType;
import com.ef.utils.InvalidArgumentsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class ArgumentsParser {

    public final static String START_DATE_TIME_FORMAT = "yyyy-MM-dd.HH:mm:ss";

    private static final Logger log = LoggerFactory.getLogger(ArgumentsParser.class);

    private final ApplicationArguments applicationArguments;

    public ArgumentsParser(ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
    }

    public File accessLog() {
        String accessLogArg = argAsString(ArgumentsType.accesslog);

        if (accessLogArg == null) {
            String message = String.format("Argument '%s' is required with absolute log file path.", ArgumentsType.accesslog);
            log.error(message);
            throw new InvalidArgumentsException(message);
        }

        File accessLogFile = new File(accessLogArg);

        if ((!accessLogFile.exists())
                || (!accessLogFile.isFile())
                || (!accessLogFile.canRead())) {
            String message = String.format("File could not be found. Invalid file path '%s' for '%s'.", accessLogArg, ArgumentsType.accesslog);
            log.error(message);
            throw new InvalidArgumentsException(message);
        }

        return accessLogFile;
    }

    public LocalDateTime startDateTime() {
        String startDateTimeArg = argAsString(ArgumentsType.startDate);

        if (startDateTimeArg == null) {
            String message = String.format("Argument '%s' is required. Date Time format is '%s'.", ArgumentsType.startDate, START_DATE_TIME_FORMAT);
            log.error(message);
            throw new InvalidArgumentsException(message);
        }

        try {
            return  LocalDateTime.parse(startDateTimeArg, DateTimeFormatter.ofPattern(START_DATE_TIME_FORMAT));
        } catch (DateTimeParseException e) {
            String message = String.format("Could not find parse '%s' as '%s'.", ArgumentsType.startDate, START_DATE_TIME_FORMAT);
            log.error(message);
            throw new InvalidArgumentsException(message);
        }
    }

    public DurationType duration() {
        String durationArg = argAsString(ArgumentsType.duration);

        if (durationArg == null) {
            String message = String.format("Argument '%s' is required. String as 'hourly' or 'daily'.", ArgumentsType.duration);
            log.error(message);
            throw new InvalidArgumentsException(message);
        }

        try {
            return DurationType.valueOf(durationArg);
        } catch (IllegalArgumentException e) {
            String message = String.format("Error parsing '%s' as 'hourly' or 'daily'.", ArgumentsType.duration);
            log.error(message);
            throw new InvalidArgumentsException(message);
        }
    }

    public int threshold() {
        String thresholdArg = argAsString(ArgumentsType.threshold);

        if (thresholdArg == null) {
            String message = String.format("Integer value is required for argument '%s'.", ArgumentsType.threshold);
            log.error(message);
            throw new InvalidArgumentsException(message);
        }

        int threshold = 0;
        try {
            threshold = new Integer(thresholdArg);
        } catch (NumberFormatException e) {
            String message = String.format("Error parsing '%s' as integer.", ArgumentsType.threshold);
            log.error(message);
            throw new InvalidArgumentsException(message);
        }

        if (threshold < 1) {
            String message = String.format("'%s' must be >= 1.", ArgumentsType.threshold);
            log.error(message);
            throw new InvalidArgumentsException(message);
        }

        return threshold;
    }

    private String argAsString(ArgumentsType argsType) {
        String argValue = null;
        List<String> argList = applicationArguments.getOptionValues(argsType.name());

        if ((argList != null) && (!argList.isEmpty())) {
            if ((argList.get(0) != null) && (!argList.get(0).trim().isEmpty())) {
                argValue = argList.get(0).trim();
            }
        }

        log.info("{} = {}", argsType, argValue);
        return argValue;
    }
}
