package com.crio.Stayease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crio.Stayease.Entity.Booking;

public interface BookingRepository extends JpaRepository<Booking,Long>{
}
