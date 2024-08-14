package com.crio.Stayease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crio.Stayease.Entity.User;

public interface UserRepository extends JpaRepository<User,Long>{
    User findByEmail(String email);
}
