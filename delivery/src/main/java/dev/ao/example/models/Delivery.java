package dev.ao.example.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;


@Entity
@Table(name = "deliveries")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Builder
public class Delivery extends UpdatableEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "timeslot_id", referencedColumnName = "id")
    private Timeslot timeSlot;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address dropoffAddress;

//    private Address pickupAddress; // TODO: add where the pickup comes from and add to calculating timeslots

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // TODO user can have multiple addresses logicaly but we dont want to pass them with the delivery

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courier_id", referencedColumnName = "id")
    private Courier courier; // TODO dont pass the Timeslots or the address of the courier with the delivery

    private DeliveryStatus status;
}
