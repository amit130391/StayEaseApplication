package com.crio.Stayease.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crio.Stayease.Dto.BookingResponseDto;
import com.crio.Stayease.Entity.Booking;
import com.crio.Stayease.Entity.Hotel;
import com.crio.Stayease.Entity.User;
import com.crio.Stayease.Exception.ResourceNotFoundException;
import com.crio.Stayease.Exception.RoomsNotAvailableException;
import com.crio.Stayease.Repository.BookingRepository;
import com.crio.Stayease.Repository.HotelRepository;
import com.crio.Stayease.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BookingService {
    
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Transactional
    public BookingResponseDto createBooking(Long userId,Long hotelId){
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not found with the given Id:" +userId));
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel not found with the given Id:" +hotelId));
        if(hotel.getAvailableRooms()<=0)
        throw new RoomsNotAvailableException("No rooms available in this hotel");
        hotel.setAvailableRooms(hotel.getAvailableRooms()-1);
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setBookingDate(LocalDate.now());
        Booking savedBooking = bookingRepository.save(booking);
        user.getBookings().add(savedBooking);
        userRepository.save(user);
        hotel.getBookings().add(savedBooking);
        hotelRepository.save(hotel);
        BookingResponseDto bookingResponseDto= new BookingResponseDto();
        bookingResponseDto.setBookingId(savedBooking.getId());
        bookingResponseDto.setHotelname(savedBooking.getHotel().getHotelName());
        bookingResponseDto.setUsername(savedBooking.getUser().getEmail());
        return bookingResponseDto;
    }

    @Transactional
    public void cancelBooking(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()->new ResourceNotFoundException("Booking not found"));
        Hotel hotel = booking.getHotel();
        hotel.getBookings().remove(booking);
        hotel.setAvailableRooms(hotel.getAvailableRooms()+1);
        hotelRepository.save(hotel);
        User customer = booking.getUser();
        customer.getBookings().remove(booking);
        userRepository.save(customer);
        bookingRepository.delete(booking);
    }

    public List<BookingResponseDto> getBookings(){
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingResponseDto> bookingResponseDtos=new ArrayList<>();
        for(Booking book:bookings){
            BookingResponseDto bookingResponseDto = new BookingResponseDto();
            bookingResponseDto.setBookingId(book.getId());
            bookingResponseDto.setHotelname(book.getHotel().getHotelName());
            bookingResponseDto.setUsername(book.getUser().getEmail());
            bookingResponseDtos.add(bookingResponseDto);
        }
        return bookingResponseDtos;
    }
}
