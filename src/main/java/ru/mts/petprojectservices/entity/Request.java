package ru.mts.petprojectservices.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Request {
    @Id
    private Integer id;

    private Integer clientId;
    private String message;
    private String address;

    private Integer executorId;
    private TypeStatus status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dateCreation;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dateLastModified;

    public Request(int clientId, String message, String address) {
        this.clientId = clientId;
        this.message = message;
        this.address = address;
    }

    public enum TypeStatus {
        CREATED,
        IN_PROGRESS,
        COMPLETED
    }

}
