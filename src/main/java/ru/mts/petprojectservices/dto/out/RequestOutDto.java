package ru.mts.petprojectservices.dto.out;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.mts.petprojectservices.entity.Client;
import ru.mts.petprojectservices.entity.Executor;
import ru.mts.petprojectservices.entity.Request;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestOutDto {
    private Integer id;
    private Client client;
    private String message;
    private String address;
    private Executor executor;
    private Request.TypeStatus status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dateCreation;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dateLastModified;

    @JsonIgnore
    private int clientId;
    @JsonIgnore
    private int executorId;
}
