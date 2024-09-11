package dev.ao.example.services;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.ao.example.dto.DeliveryRequestDto;
import dev.ao.example.models.*;
import dev.ao.example.repositories.DeliveryRepository;
import org.springframework.stereotype.Service;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.antlr.v4.runtime.misc.Utils.readFile;

@Service
public class DeliveryService {

    DeliveryRepository deliveryRepository;
    AddressService addressService;
    CourierService courierService;
    TimeslotService timeslotService;

    public DeliveryService(DeliveryRepository deliveryRepository,
                           AddressService addressService,
                           CourierService courierService,
                           TimeslotService timeslotService) {
        this.addressService = addressService;
        this.courierService = courierService;
        this.timeslotService = timeslotService;
        this.deliveryRepository = deliveryRepository;
    }

    public List<Delivery> findAllDeliveriesBetweenDates(LocalDate startDate, LocalDate endDate) {
        return deliveryRepository.findAllDeliveriesBetweenDates(startDate, endDate);
    }

    public List<Delivery> findAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Optional<Delivery> findDeliveriesById(String id) {
        return deliveryRepository.findById(id);
    }

    public Delivery findDeliveriesByIdWrapper(String id) throws Exception {
        Optional<Delivery> delivery = findDeliveriesById(id);
        if (delivery.isEmpty()) {
            throw new Exception();
        }
        return delivery.get();
    }

    public Delivery assambleDelivery(DeliveryRequestDto request) throws Exception {
//        TODO move to mapper
        Timeslot timeslot = timeslotService.findTimeslotByeIdWrapper(request.getTimeslotId());
        Delivery delivery = Delivery.builder()
                .timeSlot(timeslot)
                .dropoffAddress(addressService.findAddressesByIdWrapper(request.getAddressId()))
                .user(request.getUser())
                .courier(courierService.findCourieresByIdWrapper(timeslot.getCourier().getId()))
                .status(DeliveryStatus.BOOKED)
                .build();

        saveOrUpdateDelivery(delivery);
        return delivery;

    }

    public void updateDeliveryStatus(String deliveryId, DeliveryStatus deliveryStatus) throws Exception {
        Delivery delivery = findDeliveriesByIdWrapper(deliveryId);
        delivery.setStatus(deliveryStatus);
        saveOrUpdateDelivery(delivery);
    }

    public void saveOrUpdateDelivery(Delivery delivery) {
        deliveryRepository.save(delivery);
    }

    public void deleteDeliveryById(String id) {
        deliveryRepository.deleteById(id);
    }

    public void setUpDemo() throws Exception {
        String content = new String(readFile("./src/main/resources/static/demo.json", Charset.defaultCharset().toString()));
        JsonArray json = JsonParser.parseString(content).getAsJsonArray();
        LocalDateTime date = LocalDateTime.now().withMinute(0).withSecond(0);
        for (int i = 0; i < json.size(); i++) {
            // TODO: move to mapper
            Courier courier = new Courier();
            JsonObject jsonObject = json.get(i).getAsJsonObject();
            courier.setAddress(addressService.resolveAddress(jsonObject.get("address").getAsString()));
            courier.setName(jsonObject.get("name").getAsString());
            courier.setSurname(jsonObject.get("surname").getAsString());
            courier.setPhoneNumber(jsonObject.get("phoneNumber").getAsString());
            courier.setEmail(jsonObject.get("email").getAsString());
            courierService.saveOrUpdateCourier(courier);
            for (JsonElement days : jsonObject.get("timeslots").getAsJsonArray()) {
                LocalDateTime timeslotDate = date.plusDays(days.getAsJsonObject().get("t").getAsInt());
                for (int hour = 8; hour < 18; hour += 2) {
                    Timeslot timeslot = new Timeslot(courier, timeslotDate.withHour(hour), timeslotDate.withHour(hour + 2));
                    timeslotService.saveOrUpdateTimeslot(timeslot);
                    courier.getTimeslots().add(timeslot);
                }
            }
            courierService.saveOrUpdateCourier(courier);
        }
    }
}
