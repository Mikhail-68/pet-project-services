package ru.mts.petprojectservices.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Client {
    @Id
    private int id;
    private String fio;
}
