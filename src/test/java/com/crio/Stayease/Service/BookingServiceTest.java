package com.crio.Stayease.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.crio.Stayease.Dto.BookingResponseDto;
import com.crio.Stayease.Entity.Booking;
import com.crio.Stayease.Entity.Hotel;
import com.crio.Stayease.Entity.User;
import com.crio.Stayease.Exception.ResourceNotFoundException;
import com.crio.Stayease.Exception.RoomsNotAvailableException;
import com.crio.Stayease.Repository.BookingRepository;
import com.crio.Stayease.Repository.HotelRepository;
import com.crio.Stayease.Repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
public void testCreateBooking_Success() {
    Long userId = 1L;
    Long hotelId = 1L;

    User mockUser = new User();
    mockUser.setId(userId);
    mockUser.setEmail("test@example.com");
    mockUser.setBookings(new HashSet<>());

    Hotel mockHotel = new Hotel();
    mockHotel.setId(hotelId);
    mockHotel.setHotelName("Test Hotel");
    mockHotel.setAvailableRooms((long) 5);
    mockHotel.setBookings(new HashSet<>());

    Booking mockBooking = new Booking();
    mockBooking.setId(1L); // Set an expected ID
    mockBooking.setUser(mockUser);
    mockBooking.setHotel(mockHotel);
    mockBooking.setBookingDate(LocalDate.now());

    // Mocking the repository methods
    when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
    when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(mockHotel));
    when(bookingRepository.save(any(Booking.class))).thenReturn(mockBooking);

    // Call the method under test
    BookingResponseDto bookingResponse = bookingService.createBooking(userId, hotelId);

    // Verify and assert
    assertNotNull(bookingResponse);
    assertEquals(1L, bookingResponse.getBookingId());  // Expected ID
    assertEquals("Test Hotel", bookingResponse.getHotelname());
    assertEquals("test@example.com", bookingResponse.getUsername());

    verify(bookingRepository, times(1)).save(any(Booking.class));
    verify(userRepository, times(1)).save(mockUser);
    verify(hotelRepository, times(1)).save(mockHotel);
}


    @Test
    public void testCreateBooking_UserNotFound() {
        Long userId = 1L;
        Long hotelId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.createBooking(userId, hotelId);
        });

        assertEquals("User Not found with the given Id:1", exception.getMessage());
    }

    @Test
    public void testCreateBooking_HotelNotFound() {
        Long userId = 1L;
        Long hotelId = 1L;

        User mockUser = new User();
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.createBooking(userId, hotelId);
        });

        assertEquals("Hotel not found with the given Id:1", exception.getMessage());
    }

    @Test
    public void testCreateBooking_RoomsNotAvailable() {
        Long userId = 1L;
        Long hotelId = 1L;

        User mockUser = new User();
        mockUser.setId(userId);

        Hotel mockHotel = new Hotel();
        mockHotel.setId(hotelId);
        mockHotel.setAvailableRooms((long) 0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(mockHotel));

        RoomsNotAvailableException exception = assertThrows(RoomsNotAvailableException.class, () -> {
            bookingService.createBooking(userId, hotelId);
        });

        assertEquals("No rooms available in this hotel", exception.getMessage());
    }

    @Test
    public void testCancelBooking_Success() {
        Long bookingId = 1L;

        User mockUser = new User();
        Hotel mockHotel = new Hotel();
        mockHotel.setAvailableRooms((long) 5);

        Booking mockBooking = new Booking();
        mockBooking.setId(bookingId);
        mockBooking.setUser(mockUser);
        mockBooking.setHotel(mockHotel);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(mockBooking));

        assertDoesNotThrow(() -> bookingService.cancelBooking(bookingId));

        assertEquals(6, mockHotel.getAvailableRooms());
        verify(hotelRepository, times(1)).save(mockHotel);
        verify(userRepository, times(1)).save(mockUser);
        verify(bookingRepository, times(1)).delete(mockBooking);
    }

    @Test
    public void testCancelBooking_BookingNotFound() {
        Long bookingId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.cancelBooking(bookingId);
        });

        assertEquals("Booking not found", exception.getMessage());
    }

    @Test
    public void testGetBookings_Success() {
        List<Booking> mockBookings = new ArrayList<>();
        Booking mockBooking = new Booking();
        mockBooking.setId(1L);

        Hotel mockHotel = new Hotel();
        mockHotel.setHotelName("Hotel California");
        mockBooking.setHotel(mockHotel);

        User mockUser = new User();
        mockUser.setEmail("user@example.com");
        mockBooking.setUser(mockUser);

        mockBookings.add(mockBooking);

        when(bookingRepository.findAll()).thenReturn(mockBookings);

        List<BookingResponseDto> bookings = bookingService.getBookings();

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getBookingId());
        assertEquals("Hotel California", bookings.get(0).getHotelname());
        assertEquals("user@example.com", bookings.get(0).getUsername());
    }
}
