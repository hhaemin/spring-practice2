package com.github.supercoding.repository.passenger;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerJpaRepository extends JpaRepository<Passenger, Integer> {
    Optional<Passenger> findPassengerByUserId(Integer userId);
}
