package dev.ao.example.repositories;

import dev.ao.example.models.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, String> {

    @Query(value = "select d.* " +
            "from deliveries d " +
            "left join timeslots t on d.timeslot_id = t.id " +
            "where t.starttime >= :startDate and t.endtime <= :endDate", nativeQuery = true)
    List<Delivery> findAllDeliveriesBetweenDates(LocalDate startDate, LocalDate endDate);
}
