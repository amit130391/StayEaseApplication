package com.crio.Stayease.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.crio.Stayease.Dto.HotelDto;
import com.crio.Stayease.Entity.Hotel;
import com.crio.Stayease.Service.HotelService;

@RestController
public class HotelController {
    
    @Autowired
    HotelService hotelService;

    @GetMapping("/hotels")
    public ResponseEntity<List<Hotel>> getAvailableHotels(){

        List<Hotel> availableHotels = hotelService.getAvailableHotels();

        if(availableHotels.isEmpty())
        return ResponseEntity.noContent().build();

        return new ResponseEntity<List<Hotel>>(availableHotels, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/hotel")
    public ResponseEntity<Hotel> saveHotel(@RequestBody HotelDto hotelDto){
        Hotel hotel = hotelService.createHotel(hotelDto);
        return new ResponseEntity<Hotel>(hotel, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('HOTEL_MANAGER')")
    @PutMapping("/manager/hotel/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable("id") Long id,@RequestBody HotelDto hotelDto){
        Hotel updatedHotel = hotelService.updateHotel(id, hotelDto);
        return new ResponseEntity<Hotel>(updatedHotel, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/hotel/{id}")
    public ResponseEntity<Object> deleteHotel(@PathVariable("id") Long id){
        hotelService.deleteHotel(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
