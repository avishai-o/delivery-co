package dev.ao.example.dto;

import dev.ao.example.models.User;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequestDto {
    User user;
    String timeslotId;
    String addressId;
}
