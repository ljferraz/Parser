package com.ef.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "BLOCKED_IPS")
@Builder
@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class BlockedIps {

    @Id
    @GeneratedValue
    private Long id;

    private String ip;
    private int requestsCount;
    private String comment;

    public void incrementRequestsCount() {
        requestsCount++;
    }

}
