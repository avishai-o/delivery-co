package dev.ao.example.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnresolvedAddressRequestDto {
    String searchTerm;
}
