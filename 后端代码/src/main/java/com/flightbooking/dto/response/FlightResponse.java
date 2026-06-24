package com.flightbooking.dto.response;

import java.time.LocalDateTime;

/**
 * 航班响应DTO
 */
public class FlightResponse {

    private Long id;
    private String flightNumber;
    private String departureCity;
    private String departureCode;
    private String destinationCity;
    private String destinationCode;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Double price;
    private String duration;
    private String cabinType;
    private Integer availableSeats;
    private Integer totalSeats;
    private String airline;

    public FlightResponse() {}

    public FlightResponse(Long id, String flightNumber, String departureCity, String departureCode,
                         String destinationCity, String destinationCode, LocalDateTime departureTime,
                         LocalDateTime arrivalTime, Double price, String cabinType,
                         Integer availableSeats, Integer totalSeats, String airline, String duration) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.departureCity = departureCity;
        this.departureCode = departureCode;
        this.destinationCity = destinationCity;
        this.destinationCode = destinationCode;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.cabinType = cabinType;
        this.availableSeats = availableSeats;
        this.totalSeats = totalSeats;
        this.airline = airline;
        this.duration = duration;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getDepartureCity() { return departureCity; }
    public void setDepartureCity(String departureCity) { this.departureCity = departureCity; }

    public String getDepartureCode() { return departureCode; }
    public void setDepartureCode(String departureCode) { this.departureCode = departureCode; }

    public String getDestinationCity() { return destinationCity; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }

    public String getDestinationCode() { return destinationCode; }
    public void setDestinationCode(String destinationCode) { this.destinationCode = destinationCode; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getCabinType() { return cabinType; }
    public void setCabinType(String cabinType) { this.cabinType = cabinType; }

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public String getAirline() { return airline; }
    public void setAirline(String airline) { this.airline = airline; }
}