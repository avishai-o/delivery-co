package dev.ao.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;


@MappedSuperclass
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class Person extends UpdatableEntity {
    //    TODO add restrictions with @annotations no nulls
    private String name;
    private String surname;
    private String phoneNumber; // TODO: add validation and uniqe constraint
    private String email; // TODO: add validation and uniqe constraint
}
