package com.ef.parsers;

import com.ef.models.AccessLogFileContent;
import com.ef.models.BlockedIps;
import com.ef.utils.DurationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileParser {

    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private static final Logger log = LoggerFactory.getLogger(FileParser.class);

    private final ArgumentsParser argumentsExtractor;


    public FileParser(ArgumentsParser argumentsExtractor) {
        this.argumentsExtractor = argumentsExtractor;
    }

    public List<BlockedIps> parse() throws IOException {
        File accessLogFile = argumentsExtractor.accessLog();
        LocalDateTime startDateTime = argumentsExtractor.startDateTime();
        DurationType duration = argumentsExtractor.duration();
        int threshold = argumentsExtractor.threshold();

        LocalDateTime endDateTime = startDateTime.plusHours(duration.hours);
        Map<String, BlockedIps> accessMap = new LinkedHashMap<>();

        try (Stream linesStream = Files.lines(Paths.get(accessLogFile.toURI()), StandardCharsets.UTF_8)) {
            linesStream.forEach(line -> {
                try {
                    String[] lineArray = line.toString().split("\\|");

                    if (lineArray.length >= 2) {
                        LocalDateTime dateTime = LocalDateTime.parse(lineArray[0], DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

                        if ((startDateTime.isBefore(dateTime) || startDateTime.isEqual(dateTime))
                                && (endDateTime.isAfter(dateTime) || endDateTime.isEqual(dateTime))) {
                            String ip = lineArray[1];
                            BlockedIps blockedIps = accessMap.get(ip);

                            if (blockedIps == null) {
                                blockedIps = BlockedIps.builder().ip(ip).build();
                            }
                            blockedIps.incrementRequestsCount();
                            accessMap.put(ip, blockedIps);
                        }
                    } else {
                        log.warn("Invalid line: {}", line);
                    }
                } catch (Exception e) {
                    log.error("Error parsing line {}", line);
                }
            });
        }

        List<BlockedIps> blockedIps = accessMap.values().stream().filter(blockedIp -> blockedIp.getRequestsCount() > threshold).collect(Collectors.toList());
        blockedIps.forEach(blockedIp -> blockedIp.setComment(String.format("%s blocked because it has more than %s requests from %s to %s", blockedIp.getIp(), threshold, startDateTime, endDateTime)));

        return blockedIps;
    }

    public List<AccessLogFileContent> parseWholeFileContent() throws IOException {
        File accessLogFile = argumentsExtractor.accessLog();
        List<AccessLogFileContent> accessLogFileContentList = new ArrayList<>();

        try (Stream linesStream = Files.lines(Paths.get(accessLogFile.toURI()), StandardCharsets.UTF_8)) {
            linesStream.forEach(line -> {
                try {
                    String[] lineArray = line.toString().split("\\|");

                    if (lineArray.length >= 2) {
                        LocalDateTime dateTime = LocalDateTime.parse(lineArray[0], DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

                        AccessLogFileContent accessLogFileContent = new AccessLogFileContent();
                        accessLogFileContent.setDateTime(dateTime);
                        accessLogFileContent.setIp(lineArray[1]);
                        accessLogFileContent.setStatus(lineArray[3]);
                        accessLogFileContent.setUserAgent(lineArray[4]);

                        accessLogFileContentList.add(accessLogFileContent);
                    }  else {
                        log.warn("Invalid line: {}", line);
                    }
                } catch (Exception e) {
                    log.error("Error Parsing line {}", line);
                }
            });
        }
        return accessLogFileContentList;
    }

}
