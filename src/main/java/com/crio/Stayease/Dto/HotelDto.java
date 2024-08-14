package com.crio.Stayease.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {
    @NotEmpty(message = "Please specify hotel name")
    @NotNull(message = "Hotel name is missing")
    private String hotelName;

    @NotEmpty(message = "Please specify hotel location")
    private String location;

    @NotEmpty(message = "Please specify some description")
    private String description;

    @NotEmpty(message = "Please specify number of available rooms")
    private Long availableRooms;
}
