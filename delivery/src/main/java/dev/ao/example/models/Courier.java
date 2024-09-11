package dev.ao.example.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "couriers")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Courier extends User {
    @OneToMany(mappedBy = "courier", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Timeslot> timeslots = new ArrayList<>();
}
