package dev.ao.example.repositories;

import dev.ao.example.models.Address;
import dev.ao.example.models.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeslotRepository extends JpaRepository<Timeslot, String> {
}
