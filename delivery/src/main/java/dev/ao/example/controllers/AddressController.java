package dev.ao.example.controllers;

import dev.ao.example.dto.UnresolvedAddressRequestDto;
import dev.ao.example.models.Address;
import dev.ao.example.services.AddressService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    //    TODO: should return a list if search term is ambiguous
    @PostMapping("/resolve-address")
    Address resolveAddress(@Validated @RequestBody UnresolvedAddressRequestDto unresolvedAddress) throws Exception {
        return addressService.resolveAddress(unresolvedAddress.getSearchTerm());
    }

    @GetMapping("/{id}")
    Address getAddressById(@PathVariable String id) throws Exception {
        return addressService.findAddressesByIdWrapper(id);
    }

//   TODO for admin and test roles use only

//    @ResponseStatus(HttpStatus.CREATED)
//    @PutMapping("/create")
//    void createAddress(@Validated @RequestBody Address address) {
//        addressService.saveOrUpdateAddress(address);
//    }
//   TODO for admin and test roles use only

//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PutMapping("/update")
//    void updateAddress(@Validated @RequestBody Address address) {
//        addressService.saveOrUpdateAddress(address);
//    }

    @DeleteMapping("/{id}")
    void deleteAddressById(@PathVariable String id) {
        addressService.deleteAddressById(id);
    }
}
