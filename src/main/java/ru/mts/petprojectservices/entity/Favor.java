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
public class Favor {
    @Id
    private Integer id;
    private Integer requestId;
    private Integer executorId;
    private TypeStatus status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dateCreation;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dateLastModified;

    public Favor(Integer requestId) {
        this.requestId = requestId;
    }

    public Favor(Integer requestId, TypeStatus status) {
        this.requestId = requestId;
        this.status = status;
    }

    public Favor(Integer requestId, Integer executorId, TypeStatus status) {
        this.requestId = requestId;
        this.executorId = executorId;
        this.status = status;
    }

    public enum TypeStatus {
        CREATED,
        IN_PROGRESS,
        COMPLETED
    }
}
