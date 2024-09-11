package dev.ao.example.services;

import dev.ao.example.models.Courier;
import dev.ao.example.repositories.CourierRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class CourierService {
    CourierRepository courierRepository;

    public CourierService(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    public List<Courier> findAllCouriers() {
        return courierRepository.findAll();
    }

    public List<Courier> findAllCouriersByCountry(String country) {
        List<String> ids = courierRepository.findAllCouriersByCountry(country);
        return courierRepository.findAllById(ids);
    }

    public Optional<Courier> findCouriersById(String id) {
        return courierRepository.findById(id);
    }

    public Courier findCourieresByIdWrapper(String id) throws Exception {
        Optional<Courier> courier = findCouriersById(id);
        if (courier.isEmpty()) {
            throw new Exception();
        }
        return courier.get();
    }

    public void saveOrUpdateCourier(Courier courier) {
        courierRepository.save(courier);
    }

    public void deleteCourierById(String id) {
        courierRepository.deleteById(id);
    }
}
