package com.ef.parsers;

import com.ef.models.AccessLogFileContent;
import com.ef.models.BlockedIps;
import com.ef.models.repository.AccessLogFileContentRepository;
import com.ef.models.repository.BlockedIpsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommandLineRunnerParser implements CommandLineRunner{

    private static final Logger log = LoggerFactory.getLogger(CommandLineRunnerParser.class);

    private final FileParser fileParser;
    private final BlockedIpsRepository blockedIpsRepository;
    private final AccessLogFileContentRepository accessLogFileContentRepository;

    public CommandLineRunnerParser(FileParser fileParser, BlockedIpsRepository blockedIpsRepository, AccessLogFileContentRepository accessLogFileContentRepository) {
        this.fileParser = fileParser;
        this.blockedIpsRepository = blockedIpsRepository;
        this.accessLogFileContentRepository = accessLogFileContentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<AccessLogFileContent> accessLogFileContent = accessLogFileContentRepository.findAll();
        if (accessLogFileContent.isEmpty()) {
            accessLogFileContent = fileParser.parseWholeFileContent();
            accessLogFileContentRepository.saveAll(accessLogFileContent);
        }

        List<BlockedIps> blockedIps = fileParser.parse();

        log.info("Blocked IPs: {}", blockedIps.stream().map(blockedIp -> blockedIp.getIp()).collect(Collectors.joining(", ")));
        blockedIpsRepository.saveAll(blockedIps);
    }
}
