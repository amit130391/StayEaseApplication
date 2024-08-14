package com.crio.Stayease.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.crio.Stayease.Dto.HotelDto;
import com.crio.Stayease.Entity.Hotel;
import com.crio.Stayease.Exception.HotelPresentInDatabaseException;
import com.crio.Stayease.Exception.ResourceNotFoundException;
import com.crio.Stayease.Repository.HotelRepository;

@Service
public class HotelService {

    @Autowired
    HotelRepository hotelRepository;

    public Hotel createHotel(HotelDto hotelDto){
        Hotel hotel = new Hotel();
        hotel.setHotelName(hotelDto.getHotelName());
        hotel.setLocation(hotelDto.getLocation());
        hotel.setDescription(hotelDto.getDescription());
        hotel.setAvailableRooms(hotelDto.getAvailableRooms());

        try {
            hotelRepository.save(hotel);
        } catch (DataIntegrityViolationException e) {
            throw new HotelPresentInDatabaseException("Hotel with the same name is already present in the database");
        }
        return hotel;
    }

    public List<Hotel> getAvailableHotels(){
        List<Hotel> availableHotels = hotelRepository.findAll().stream().filter(hotel->hotel.getAvailableRooms()>0).collect(Collectors.toList());
        return availableHotels;
    }

    public Hotel updateHotel(Long id,HotelDto updatedHotel){
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel not found for the given id:"+id));
        hotel.setHotelName(updatedHotel.getHotelName());
        hotel.setLocation(updatedHotel.getLocation());
        hotel.setDescription(updatedHotel.getDescription());
        hotel.setAvailableRooms(updatedHotel.getAvailableRooms());
        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id){
        hotelRepository.deleteById(id);
    }
}
