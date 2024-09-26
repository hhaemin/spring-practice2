package com.github.supercoding.repository.passenger;

public interface PassengerRepository {
    Passenger findPassengerByUserId(Integer userId);
}
