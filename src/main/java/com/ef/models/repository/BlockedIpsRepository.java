package com.ef.models.repository;

import com.ef.models.BlockedIps;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedIpsRepository extends JpaRepository<BlockedIps, Long> {

}
