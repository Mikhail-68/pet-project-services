package ru.mts.petprojectservices.dto.out;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.mts.petprojectservices.entity.Client;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "clientId"})
public class RequestOutDto {
    private Integer id;
    private Client client;
    private String message;
    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dateCreation;

    @JsonIgnore
    private Integer clientId;
}
