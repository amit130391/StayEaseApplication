package com.crio.Stayease.Service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.crio.Stayease.Dto.HotelDto;
import com.crio.Stayease.Entity.Hotel;
import com.crio.Stayease.Exception.HotelPresentInDatabaseException;
import com.crio.Stayease.Exception.ResourceNotFoundException;
import com.crio.Stayease.Repository.HotelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateHotel_Success() {
        HotelDto hotelDto = new HotelDto();
        hotelDto.setHotelName("Hotel California");
        hotelDto.setLocation("California");
        hotelDto.setDescription("A lovely place");
        hotelDto.setAvailableRooms((long) 10);

        when(hotelRepository.save(any(Hotel.class))).thenReturn(new Hotel());

        Hotel hotel = hotelService.createHotel(hotelDto);

        assertNotNull(hotel);
        assertEquals("Hotel California", hotel.getHotelName());
        assertEquals("California", hotel.getLocation());
        assertEquals("A lovely place", hotel.getDescription());
        assertEquals(10, hotel.getAvailableRooms());
    }

    @Test
    public void testCreateHotel_HotelAlreadyExists() {
        HotelDto hotelDto = new HotelDto();
        hotelDto.setHotelName("Hotel California");

        when(hotelRepository.save(any(Hotel.class))).thenThrow(new DataIntegrityViolationException(""));

        HotelPresentInDatabaseException exception = assertThrows(HotelPresentInDatabaseException.class, () -> {
            hotelService.createHotel(hotelDto);
        });

        assertEquals("Hotel with the same name is already present in the database", exception.getMessage());
    }

    @Test
    public void testGetAvailableHotels_Success() {
        List<Hotel> mockHotels = new ArrayList<>();
        mockHotels.add(new Hotel(1, "Hotel California", "California", "A lovely place",10));
        mockHotels.add(new Hotel(2,"Hotel Empty", "Nevada", "Not a lovely place", 0));

        when(hotelRepository.findAll()).thenReturn(mockHotels);

        List<Hotel> availableHotels = hotelService.getAvailableHotels();

        assertNotNull(availableHotels);
        assertEquals(1, availableHotels.size());
        assertEquals("Hotel California", availableHotels.get(0).getHotelName());
    }

    @Test
    public void testUpdateHotel_Success() {
        Long hotelId = 1L;
        HotelDto updatedHotelDto = new HotelDto();
        updatedHotelDto.setHotelName("Updated Hotel");
        updatedHotelDto.setLocation("Updated Location");
        updatedHotelDto.setDescription("Updated Description");
        updatedHotelDto.setAvailableRooms((long) 5);

        Hotel existingHotel = new Hotel(1,"Old Hotel", "Old Location", "Old Description", 10);
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(existingHotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(existingHotel);

        Hotel updatedHotel = hotelService.updateHotel(hotelId, updatedHotelDto);

        assertNotNull(updatedHotel);
        assertEquals("Updated Hotel", updatedHotel.getHotelName());
        assertEquals("Updated Location", updatedHotel.getLocation());
        assertEquals("Updated Description", updatedHotel.getDescription());
        assertEquals(5, updatedHotel.getAvailableRooms());
    }

    @Test
    public void testUpdateHotel_HotelNotFound() {
        Long hotelId = 1L;
        HotelDto updatedHotelDto = new HotelDto();

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            hotelService.updateHotel(hotelId, updatedHotelDto);
        });

        assertEquals("Hotel not found for the given id:1", exception.getMessage());
    }

    @Test
    public void testDeleteHotel_Success() {
        Long hotelId = 1L;

        doNothing().when(hotelRepository).deleteById(hotelId);

        assertDoesNotThrow(() -> hotelService.deleteHotel(hotelId));
    }
}

