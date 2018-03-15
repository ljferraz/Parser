package com.ef.models.repository;

import com.ef.models.AccessLogFileContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogFileContentRepository extends JpaRepository<AccessLogFileContent, Long> {
}
