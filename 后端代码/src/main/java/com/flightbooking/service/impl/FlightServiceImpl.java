package com.flightbooking.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.flightbooking.dto.response.FlightResponse;
import com.flightbooking.entity.Flight;
import com.flightbooking.repository.FlightRepository;
import com.flightbooking.service.FlightService;

/**
 * 航班服务实现类
 */
@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public List<FlightResponse> searchFlights(String departureCity, String destinationCity,
                                              String departDate, String sortBy) {
        LocalDate targetDate = LocalDate.parse(departDate);
        
        // 查询固定航班（每天都有）
        List<Flight> regularFlights = flightRepository.findRegularFlights(departureCity, destinationCity);
        
        // 查询当天的特殊航班（特定日期才有）
        List<Flight> specialFlights = flightRepository.findSpecialFlights(departureCity, destinationCity, targetDate);
        
        // 合并航班
        List<FlightResponse> allFlights = new ArrayList<>();
        
        // 处理固定航班（调整日期到目标日期）
        for (Flight flight : regularFlights) {
            allFlights.add(convertToResponseWithDate(flight, targetDate));
        }
        
        // 处理特殊航班（保持原日期）
        for (Flight flight : specialFlights) {
            allFlights.add(convertToResponse(flight));
        }
        
        // 根据排序参数进行排序
        switch (sortBy) {
            case "duration":
                // 按飞行时长排序（时间最短在前）
                allFlights.sort(Comparator.comparing(this::parseDurationMinutes));
                break;
            case "price":
                // 按票价排序（价格最低在前）
                allFlights.sort(Comparator.comparing(FlightResponse::getPrice));
                break;
            case "departureTime":
            default:
                // 按出发时间排序（默认）
                allFlights.sort(Comparator.comparing(FlightResponse::getDepartureTime));
                break;
        }
        
        return allFlights;
    }
    
    /**
     * 解析时长字符串为分钟数
     */
    private long parseDurationMinutes(FlightResponse flight) {
        String duration = flight.getDuration();
        long hours = 0, minutes = 0;
        
        if (duration.contains("小时")) {
            hours = Long.parseLong(duration.split("小时")[0].trim());
        }
        if (duration.contains("分钟")) {
            String minPart = duration.contains("小时") ? duration.split("小时")[1] : duration;
            minutes = Long.parseLong(minPart.split("分钟")[0].trim());
        }
        return hours * 60 + minutes;
    }

    /**
     * 将固定航班的时间调整到目标日期
     */
    private FlightResponse convertToResponseWithDate(Flight flight, LocalDate targetDate) {
        LocalDateTime baseDeparture = flight.getDepartureTime();
        LocalDateTime baseArrival = flight.getArrivalTime();
        
        LocalTime departTime = baseDeparture.toLocalTime();
        LocalTime arrivalTime = baseArrival.toLocalTime();
        
        LocalDateTime newDeparture = LocalDateTime.of(targetDate, departTime);
        LocalDateTime newArrival = LocalDateTime.of(targetDate, arrivalTime);
        
        // 计算飞行时长
        long departureMinutes = departTime.toSecondOfDay() / 60;
        long arrivalMinutes = arrivalTime.toSecondOfDay() / 60;
        
        // 处理跨天情况
        long durationMinutes;
        if (arrivalMinutes >= departureMinutes) {
            durationMinutes = arrivalMinutes - departureMinutes;
        } else {
            durationMinutes = (24 * 60 - departureMinutes) + arrivalMinutes;
            newArrival = newArrival.plusDays(1);
        }
        
        // 格式化时长
        String duration = formatDuration(durationMinutes);

        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getDepartureCity(),
                flight.getDepartureCode(),
                flight.getDestinationCity(),
                flight.getDestinationCode(),
                newDeparture,
                newArrival,
                flight.getPrice(),
                flight.getCabinType(),
                flight.getAvailableSeats(),
                flight.getTotalSeats(),
                flight.getAirline(),
                duration
        );
    }

    /**
     * 直接转换（用于特殊航班）
     */
    private FlightResponse convertToResponse(Flight flight) {
        // 计算飞行时长
        long durationMinutes = java.time.Duration.between(
                flight.getDepartureTime(), 
                flight.getArrivalTime()
        ).toMinutes();
        
        String duration = formatDuration(durationMinutes);
        
        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getDepartureCity(),
                flight.getDepartureCode(),
                flight.getDestinationCity(),
                flight.getDestinationCode(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getPrice(),
                flight.getCabinType(),
                flight.getAvailableSeats(),
                flight.getTotalSeats(),
                flight.getAirline(),
                duration
        );
    }

    /**
     * 格式化时长（分钟转成"X小时Y分钟"格式）
     */
    private String formatDuration(long minutes) {
        long hours = minutes / 60;
        long mins = minutes % 60;
        
        if (hours > 0 && mins > 0) {
            return hours + "小时" + mins + "分钟";
        } else if (hours > 0) {
            return hours + "小时";
        } else {
            return mins + "分钟";
        }
    }
}