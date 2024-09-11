package dev.ao.example.controllers;

import dev.ao.example.models.Courier;
import dev.ao.example.services.AddressService;
import dev.ao.example.services.CourierService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courier")
public class CourierController {

    private final CourierService courierService;
    private final AddressService addressService;

    public CourierController(CourierService courierService,
                             AddressService addressService) {
        this.courierService = courierService;
        this.addressService = addressService;
    }

    @GetMapping("/")
    List<Courier> getAllCouriers() {
        return courierService.findAllCouriers();
    }

    @GetMapping("/{id}")
    Courier getCourierById(@PathVariable String id) throws Exception {
        return courierService.findCourieresByIdWrapper(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/create")
    void createCourier(@Validated @RequestBody Courier courier, @RequestParam String addressId) throws Exception {
        courier.setAddress(addressService.findAddressesByIdWrapper(addressId));
        courierService.saveOrUpdateCourier(courier);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/update")
    void updateCourier(@Validated @RequestBody Courier courier) {
        courierService.saveOrUpdateCourier(courier);
    }

    @DeleteMapping("/{id}")
    void deleteCourierById(@PathVariable String id) {
        courierService.deleteCourierById(id);
    }
}
