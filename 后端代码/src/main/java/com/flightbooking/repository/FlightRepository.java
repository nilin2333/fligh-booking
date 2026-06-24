package com.flightbooking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flightbooking.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {

//查询固定航班（不限制日期，每天都有）
    @Query("SELECT f FROM Flight f WHERE " +
           "f.departureCity = :departureCity AND " +
           "f.destinationCity = :destinationCity AND " +
           "f.flightType = 'regular' AND " +
           "f.status = 1 " +
           "ORDER BY f.departureTime ASC")
    List<Flight> findRegularFlights(
            @Param("departureCity") String departureCity,
            @Param("destinationCity") String destinationCity
    );

//查询固定航班（带舱位筛选）
    @Query("SELECT f FROM Flight f WHERE " +
           "f.departureCity = :departureCity AND " +
           "f.destinationCity = :destinationCity AND " +
           "f.flightType = 'regular' AND " +
           "f.cabinType = :cabinType AND " +
           "f.status = 1 " +
           "ORDER BY f.departureTime ASC")
    List<Flight> findRegularFlightsWithCabin(
            @Param("departureCity") String departureCity,
            @Param("destinationCity") String destinationCity,
            @Param("cabinType") String cabinType
    );

//查询特殊航班（特定日期才有）
    @Query("SELECT f FROM Flight f WHERE " +
           "f.departureCity = :departureCity AND " +
           "f.destinationCity = :destinationCity AND " +
           "f.flightType = 'special' AND " +
           "FUNCTION('DATE', f.departureTime) = :departDate AND " +
           "f.status = 1 " +
           "ORDER BY f.departureTime ASC")
    List<Flight> findSpecialFlights(
            @Param("departureCity") String departureCity,
            @Param("destinationCity") String destinationCity,
            @Param("departDate") LocalDate departDate
    );

//查询特殊航班（带舱位筛选）
    @Query("SELECT f FROM Flight f WHERE " +
           "f.departureCity = :departureCity AND " +
           "f.destinationCity = :destinationCity AND " +
           "f.flightType = 'special' AND " +
           "FUNCTION('DATE', f.departureTime) = :departDate AND " +
           "f.cabinType = :cabinType AND " +
           "f.status = 1 " +
           "ORDER BY f.departureTime ASC")
    List<Flight> findSpecialFlightsWithCabin(
            @Param("departureCity") String departureCity,
            @Param("destinationCity") String destinationCity,
            @Param("departDate") LocalDate departDate,
            @Param("cabinType") String cabinType
    );
}