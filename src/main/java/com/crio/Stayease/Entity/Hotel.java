package com.crio.Stayease.Entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false,unique = true)
    private String hotelName;

    private String location;

    private String description;

    private Long availableRooms;

    @OneToMany(mappedBy = "hotel",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Booking> bookings=new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(Id); // Use only the id
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(Id, hotel.Id);
    }

    public Hotel(long id, String hotelName, String location, String description, long availableRooms) {
        this.Id=id;
        this.hotelName=hotelName;
        this.location=location;
        this.description=description;
        this.availableRooms=availableRooms;
    }
}
