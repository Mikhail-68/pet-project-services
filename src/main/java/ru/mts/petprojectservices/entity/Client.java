package ru.mts.petprojectservices.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Client {
    @Id
    private int id;
    private String fio;
}
