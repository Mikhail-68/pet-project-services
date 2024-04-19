package ru.mts.petprojectservices.dto.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestInDto {
    private int clientId;
    private String message;
    private String address;
}
