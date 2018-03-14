package com.ef.parsers;

import com.ef.models.BlockedIps;
import com.ef.models.repository.BlockedIpsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommandLineRunnerParser implements CommandLineRunner{

    private static final Logger log = LoggerFactory.getLogger(CommandLineRunner.class);

    private final FileParser fileParser;
    private final BlockedIpsRepository blockedIpsRepository;

    public CommandLineRunnerParser(FileParser fileParser, BlockedIpsRepository blockedIpsRepository) {
        this.fileParser = fileParser;
        this.blockedIpsRepository = blockedIpsRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<BlockedIps> blockedIps = fileParser.parse();
        log.info("Blocked IP: {}", blockedIps.stream().map(blockedIp -> blockedIp.getIp()).collect(Collectors.joining(", ")));
        blockedIpsRepository.saveAll(blockedIps);
    }
}
