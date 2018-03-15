package com.ef.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "ACCESS_LOG_FILE_CONTENT")
@Builder
@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class AccessLogFileContent {

    @Id
    @GeneratedValue
    private int id;

    private LocalDateTime dateTime;
    private String ip;
    private String status;
    private String userAgent;

}
