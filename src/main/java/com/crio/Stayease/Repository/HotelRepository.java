package com.crio.Stayease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crio.Stayease.Entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel,Long>{
    
}
