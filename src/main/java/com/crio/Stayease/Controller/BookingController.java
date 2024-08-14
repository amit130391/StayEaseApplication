package com.crio.Stayease.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crio.Stayease.Dto.BookingResponseDto;
import com.crio.Stayease.Service.BookingService;
import com.crio.Stayease.Service.UserService;

@RestController
public class BookingController {

    @Autowired
    UserService userService;

    @Autowired
    BookingService bookingService;
    
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("user/me/hotels/{hotelId}")
    public ResponseEntity<BookingResponseDto> createBooking(@PathVariable("hotelId") Long hotelId,@AuthenticationPrincipal UserDetails userDetails){
        Long userId = userService.getUser(userDetails.getUsername()).getId();
        BookingResponseDto booking = bookingService.createBooking(userId, hotelId);
        return new ResponseEntity<BookingResponseDto>(booking, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('HOTEL_MANAGER')")
    @DeleteMapping("manager/bookings/{bookingId}")
    public ResponseEntity<Object> cancelBooking(@PathVariable("bookingId") Long bookingId){
        bookingService.cancelBooking(bookingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/bookings")
    public ResponseEntity<List<BookingResponseDto>> getBookings(){
        List<BookingResponseDto> bookings = bookingService.getBookings();
        if(bookings.isEmpty())
        return ResponseEntity.noContent().build();

        return new ResponseEntity<List<BookingResponseDto>>(bookings, HttpStatus.OK);
    }

    
}
