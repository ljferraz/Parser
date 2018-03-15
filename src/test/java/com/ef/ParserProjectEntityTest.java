package com.ef;

import com.ef.models.repository.AccessLogFileContentRepository;
import com.ef.models.repository.BlockedIpsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ParserProjectEntityTest {

    @Autowired
    private BlockedIpsRepository blockedIpsRepository;

    @Autowired
    private AccessLogFileContentRepository accessLogFileContentRepository;

    @Test
    public void blockedIpsCheckData() {
        Long count = blockedIpsRepository.count();
        System.out.println("Data on BLOCKED_IPS table: " + count + " rows.");
    }

    @Test
    public void accessLogFileContentCheckData() {
        Long count = accessLogFileContentRepository.count();
        System.out.println("Data on ACCESS_LOG_FILE_CONTENT table: " + count + " rows.");
    }
}
