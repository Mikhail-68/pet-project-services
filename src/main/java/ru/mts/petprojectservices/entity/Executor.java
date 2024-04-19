package ru.mts.petprojectservices.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Executor {
    @Id
    @EqualsAndHashCode.Exclude
    private int id;
    private String fio;
}
