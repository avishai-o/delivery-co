package dev.ao.example.controllers;

import dev.ao.example.dto.DeliveryRequestDto;
import dev.ao.example.models.Delivery;
import dev.ao.example.models.DeliveryStatus;
import dev.ao.example.services.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/{deliveryId}/start")
    void startDelivery(@PathVariable String deliveryId) throws Exception {
        deliveryService.updateDeliveryStatus(deliveryId, DeliveryStatus.IN_PROGRESS);
    }

    @PostMapping("/{deliveryId}/cancel")
    void cancelDelivery(@PathVariable String deliveryId) throws Exception {
        deliveryService.updateDeliveryStatus(deliveryId, DeliveryStatus.CANCELED);
    }

    @PostMapping("/{deliveryId}/complete")
    void completeDelivery(@PathVariable String deliveryId) throws Exception {
        deliveryService.updateDeliveryStatus(deliveryId, DeliveryStatus.COMPLETED);
    }

    @GetMapping("/daily")
    List<Delivery> getDailyDeliveries() {
        LocalDate startDate = LocalDate.now();
        return deliveryService.findAllDeliveriesBetweenDates(startDate, startDate);
    }

    @GetMapping("/weekly")
    List<Delivery> getWeeklyDeliveries() {
        LocalDate startDate = LocalDate.now();
        return deliveryService.findAllDeliveriesBetweenDates(startDate, startDate.plusDays(6));
    }

    //   TODO: for admin and test roles use only
    @GetMapping("/")
    List<Delivery> getAllDeliveries() {
        return deliveryService.findAllDeliveries();
    }

    @GetMapping("/{id}")
    Delivery getDeliveryById(@PathVariable String id) throws Exception {
        return deliveryService.findDeliveriesByIdWrapper(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    void createDelivery(@Validated @RequestBody DeliveryRequestDto request) throws Exception {
        deliveryService.assambleDelivery(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/update")
    void updateDelivery(@Validated @RequestBody Delivery delivery) {
        deliveryService.saveOrUpdateDelivery(delivery);
    }

    @DeleteMapping("/{id}")
    void deleteDeliveryById(@PathVariable String id) {
        deliveryService.deleteDeliveryById(id);
    }


    //  TODO: for admin and test roles use only
    @GetMapping("/setdemo")
    void setUpDemo() throws Exception {
        deliveryService.setUpDemo();
    }
}
