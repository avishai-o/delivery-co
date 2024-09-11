package dev.ao.example.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.ao.example.models.Address;
import dev.ao.example.repositories.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
public class AddressService {

    final static String GEOAPIFY_SEARCH = "/v1/geocode/search";
    AddressRepository addressRepository;
    private final WebClient geoApify;

    public AddressService(AddressRepository addressRepository, WebClient geoApify) {
        this.addressRepository = addressRepository;
        this.geoApify = geoApify;
    }

/*    TODO: geoapify can return multiple results for an ambiguous search term,
      should be changed to return multi addresses to the user so he could select the
      right one or retry with a different search term */
    public Address resolveAddress(String unresolvedAddress) throws Exception {
//   TODO: move constants to config
        String result = geoApify.get()
                .uri(builder -> builder
                        .path(GEOAPIFY_SEARCH)
                        .queryParam("text", unresolvedAddress)
                        .queryParam("apiKey", "dff76341a7b241f1a2b42944eef352a1")
                        .build()
                ).exchange()
                .block()
                .bodyToMono(String.class)
                .block();
        JsonObject json = JsonParser.parseString(result).getAsJsonObject();
        JsonArray featuresJson = json.get("features").getAsJsonArray();
        if (featuresJson.size() > 1) {
            throw new Exception("address ambiguous");
        }
//        TODO: move to mapper set what fields are a must and check if property exists
        JsonObject propertiesJson = featuresJson.get(0).getAsJsonObject().get("properties").getAsJsonObject();

        Address resolvedAddress = Address.builder()
                .country(propertiesJson.get("country").getAsString())
                .countryCode(propertiesJson.get("country_code").getAsString())
                .state(propertiesJson.get("state").getAsString())
                .city(propertiesJson.get("city").getAsString())
                .streetName(propertiesJson.get("street").getAsString())
                .postalCode(propertiesJson.get("postcode").getAsString())
                .houseNumber(propertiesJson.get("housenumber").getAsInt())
                .addressLine1(propertiesJson.get("address_line1").getAsString())
                .addressLine2(propertiesJson.get("address_line2").getAsString())
                .latitude(propertiesJson.get("lat").getAsString())
                .longitude(propertiesJson.get("lon").getAsString())
                .timezone(propertiesJson.get("timezone").getAsJsonObject().get("offset_STD").getAsString())
                .build();
        addressRepository.save(resolvedAddress);

        return resolvedAddress;
    }

    public Optional<Address> findAddressesById(String id) {
        return addressRepository.findById(id);
    }

    public Address findAddressesByIdWrapper(String id) throws Exception {
        Optional<Address> address = findAddressesById(id);
        if (address.isEmpty()) {
            throw new Exception();
        }
        return address.get();
    }

    public void saveOrUpdateAddress(Address address) {
        addressRepository.save(address);
    }

    public void deleteAddressById(String id) {
        addressRepository.deleteById(id);
    }

}
