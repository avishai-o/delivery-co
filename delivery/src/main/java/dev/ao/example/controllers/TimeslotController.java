package dev.ao.example.controllers;

import com.github.agogs.holidayapi.model.Holiday;
import dev.ao.example.models.Address;
import dev.ao.example.models.Timeslot;
import dev.ao.example.services.AddressService;
import dev.ao.example.services.TimeslotService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timeslot")
public class TimeslotController {

    private final TimeslotService timeslotService;
    private final AddressService addressService;

    public TimeslotController(TimeslotService timeslotService,
                              AddressService addressService) {
        this.timeslotService = timeslotService;
        this.addressService = addressService;
    }


    @GetMapping("/holidays/{addressId}")
    List<Holiday> getAllHolidaysByAddressId(@PathVariable String addressId) throws Exception {
        Address address = addressService.findAddressesByIdWrapper(addressId);
        return timeslotService.getHolidaysForDates(address);
    }

    @PostMapping("/timeslots")
    List<Timeslot> getTimeslotsByAddress(@RequestBody Address address) throws Exception {
        return timeslotService.getTimeslotsByAddress(address);
    }

    @GetMapping("/timeslots/{addressId}")
    List<Timeslot> getTimeslotsByAddressId(@PathVariable String addressId) throws Exception {
        Address address = addressService.findAddressesByIdWrapper(addressId);
        return timeslotService.getTimeslotsByAddress(address);
    }

    //   TODO for admin and test roles use only
    @GetMapping("/")
    List<Timeslot> getAllTimeslots() {
        return timeslotService.findAllTimeslots();
    }

    @GetMapping("/{id}")
    Timeslot getTimeslotById(@PathVariable String id) throws Exception {
        return timeslotService.findTimeslotByeIdWrapper(id);
    }

    //   TODO for courier, admin and test roles use only
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/create")
    void createTimeslot(@Validated @RequestBody Timeslot timeslot) {
        timeslotService.saveOrUpdateTimeslot(timeslot);
    }

    //   TODO for courier, admin and test roles use only
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/update")
    void updateTimeslot(@Validated @RequestBody Timeslot timeslot) {
        timeslotService.saveOrUpdateTimeslot(timeslot);
    }

    @DeleteMapping("/{id}")
    void deleteTimeslotById(@PathVariable String id) {
        timeslotService.deleteTimeslotById(id);
    }
}
