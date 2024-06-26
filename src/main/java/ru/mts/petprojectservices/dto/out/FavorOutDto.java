package ru.mts.petprojectservices.dto.out;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.mts.petprojectservices.entity.Executor;
import ru.mts.petprojectservices.entity.Favor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "clientId", "executorId", "requestId"})
public class FavorOutDto {
    private Integer id;
    private RequestOutDto request;
    private Executor executor;
    private Favor.TypeStatus status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dateCreation;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dateLastModified;

    @JsonIgnore
    private int clientId;
    @JsonIgnore
    private int executorId;
    @JsonIgnore
    private int requestId;
}
