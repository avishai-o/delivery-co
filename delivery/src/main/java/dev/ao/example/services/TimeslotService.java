package dev.ao.example.services;


import com.github.agogs.holidayapi.api.APIConsumer;
import com.github.agogs.holidayapi.model.Holiday;
import com.github.agogs.holidayapi.model.HolidayAPIResponse;
import com.github.agogs.holidayapi.model.QueryParams;
import dev.ao.example.models.Address;
import dev.ao.example.models.Courier;
import dev.ao.example.models.Timeslot;
import dev.ao.example.repositories.TimeslotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class TimeslotService {
    //    TODO: move constants to constants
    final static double EARTH_RADIUS = 6371;
    final static double RANGE_LIMIT = 800;

    TimeslotRepository timeslotRepository;
    CourierService courierService;
    private final APIConsumer holidayApi;

    public TimeslotService(TimeslotRepository timeslotRepository, APIConsumer holidayApi, CourierService courierService) {
        this.timeslotRepository = timeslotRepository;
        this.courierService = courierService;
        this.holidayApi = holidayApi;
    }

    public List<Timeslot> getTimeslotsByAddress(Address address) throws Exception {
//        TODO: make the request parallel
        List<Holiday> holidays = getHolidaysForDates(address);
//        filtering by country
        List<Courier> couriers = courierService.findAllCouriersByCountry(address.getCountry());
//        filtering by range or state
        couriers = couriers.stream().filter(courier -> isInRange(courier.getAddress(), address)).toList();
//        filtering by holidays     TODO: filter timeslots that are fully booked(2 uses)
        return couriers.stream()
                .flatMap(courier -> courier.getTimeslots().stream())
                .filter(timeslot -> !isHoliday(timeslot.getStartTime().toLocalDate(), holidays))
                .toList();

    }

    private static boolean isHoliday(LocalDate timeslotDate, List<Holiday> holidays) {
        for (Holiday holiday : holidays) {
            if (LocalDate.parse(holiday.getDate()).equals(timeslotDate)) { // Assuming date is string; adjust if needed
                return true;
            }
        }
        return false;
    }

    private static boolean isInRange(Address courierAddress, Address address) {
        if (courierAddress.getLatitude() == null || courierAddress.getLongitude() == null ||
                address.getLatitude() == null || address.getLongitude() == null) {
            if (courierAddress.getState() == address.getState()) {
                return true;
            }
        } else if (
                calculateDistance(Double.parseDouble(courierAddress.getLatitude()),
                        Double.parseDouble(courierAddress.getLongitude()),
                        Double.parseDouble(address.getLatitude()),
                        Double.parseDouble(address.getLongitude())) < RANGE_LIMIT) {
            return true;
        }
        return false;
    }

    static private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        double distance = Math.sqrt(x * x + y * y) * EARTH_RADIUS;

        return distance;
    }


    public List<Holiday> getHolidaysForDates(Address address) throws Exception {
        List<Holiday> holidays = null;
//        the free holiday api allows only request for last year or before that else it returns 402.
        ZonedDateTime date = ZonedDateTime.now(ZoneId.of(address.getTimezone())).minusYears(1);
        QueryParams params = new QueryParams();
//   TODO: move constants to config
        params.key("6136adcc-69e0-46ca-99ab-9de039a41a12")
                .month(date.getMonthValue())
                .country(QueryParams.Country.valueOf(address.getCountry().toUpperCase().replace(" ", "_")))
                .year(date.getYear());
        try {
            HolidayAPIResponse response = holidayApi.getHolidays(params);
            int status = response.getStatus();
            if (status != 200) {
                //TODO: handle error scenario (retry or throw exception)
            } else {
                holidays = response.getHolidays();
            }
        } catch (Exception e) {
            //TODO: create specific exception
        }

        return holidays;
    }

    public List<Timeslot> findAllTimeslots() {
        return timeslotRepository.findAll();
    }

    public Optional<Timeslot> findTimeslotByeId(String id) {
        return timeslotRepository.findById(id);
    }

    public Timeslot findTimeslotByeIdWrapper(String id) throws Exception {
        Optional<Timeslot> timeslot = findTimeslotByeId(id);
        if (timeslot.isEmpty()) {
            throw new Exception();
        }
        return timeslot.get();
    }

    public void saveOrUpdateTimeslot(Timeslot timeslot) {
        timeslotRepository.save(timeslot);
    }

    public void deleteTimeslotById(String id) {
        timeslotRepository.deleteById(id);
    }

}
