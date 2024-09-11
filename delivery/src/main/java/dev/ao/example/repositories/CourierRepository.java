package dev.ao.example.repositories;

import dev.ao.example.models.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourierRepository extends JpaRepository<Courier, String> {

    //    TODO: make it return the courier not just the ids
    @Query(value = "select c.id " +
            "from couriers c " +
            "left join addresses a on c.address_id = a.id " +
            "where a.country = :country", nativeQuery = true)
    public List<String> findAllCouriersByCountry(String country);
}
