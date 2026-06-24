package com.flightbooking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flightbooking.dto.response.ApiResponse;
import com.flightbooking.dto.response.FlightResponse;
import com.flightbooking.entity.Flight;
import com.flightbooking.repository.FlightRepository;
import com.flightbooking.service.FlightService;

/**
 * 航班控制器
 */
@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "*")
public class FlightController {

    private final FlightService flightService;
    private final FlightRepository flightRepository;

    public FlightController(FlightService flightService, FlightRepository flightRepository) {
        this.flightService = flightService;
        this.flightRepository = flightRepository;
    }
    

    /**
     * 搜索航班
     * @param departure 出发城市
     * @param destination 到达城市
     * @param date 出发日期
     * @param sortBy 排序方式（departureTime-按出发时间, duration-按经过时间, price-按票价）
     * @return 航班列表
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FlightResponse>>> searchFlights(
            @RequestParam String departure,
            @RequestParam String destination,
            @RequestParam String date,
            @RequestParam(defaultValue = "departureTime") String sortBy) {
        
        List<FlightResponse> flights = flightService.searchFlights(departure, destination, date, sortBy);
        
        return ResponseEntity.ok(ApiResponse.success("查询成功", flights));
    }

    /**
     * 获取所有航班列表（管理员使用）
     * @param flightNumber 航班号（可选，模糊匹配）
     * @param departure 出发地（可选，模糊匹配城市名或机场代码）
     * @param destination 目的地（可选，模糊匹配城市名或机场代码）
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Flight>>> getAllFlights(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String departure,
            @RequestParam(required = false) String destination) {
        try {
            List<Flight> flights = flightRepository.findAll();
            
            // 按航班号搜索
            if (flightNumber != null && !flightNumber.trim().isEmpty()) {
                String keyword = flightNumber.trim().toLowerCase();
                flights = flights.stream()
                        .filter(f -> f.getFlightNumber() != null && f.getFlightNumber().toLowerCase().contains(keyword))
                        .toList();
            }
            
            // 按出发地搜索（匹配城市名或机场代码）
            if (departure != null && !departure.trim().isEmpty()) {
                String keyword = departure.trim().toLowerCase();
                flights = flights.stream()
                        .filter(f -> 
                            (f.getDepartureCity() != null && f.getDepartureCity().toLowerCase().contains(keyword)) ||
                            (f.getDepartureCode() != null && f.getDepartureCode().toLowerCase().contains(keyword))
                        )
                        .toList();
            }
            
            // 按目的地搜索（匹配城市名或机场代码）
            if (destination != null && !destination.trim().isEmpty()) {
                String keyword = destination.trim().toLowerCase();
                flights = flights.stream()
                        .filter(f -> 
                            (f.getDestinationCity() != null && f.getDestinationCity().toLowerCase().contains(keyword)) ||
                            (f.getDestinationCode() != null && f.getDestinationCode().toLowerCase().contains(keyword))
                        )
                        .toList();
            }
            
            return ResponseEntity.ok(ApiResponse.success("查询成功", flights));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 添加航班（管理员使用）
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse<Flight>> addFlight(@RequestBody Flight flight) {
        try {
            if (flight.getAvailableSeats() == null) {
                flight.setAvailableSeats(flight.getTotalSeats());
            }
            Flight savedFlight = flightRepository.save(flight);
            return ResponseEntity.ok(ApiResponse.success("添加成功", savedFlight));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("添加失败: " + e.getMessage()));
        }
    }

    /**
     * 删除航班（管理员使用）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFlight(@PathVariable Long id) {
        try {
            if (!flightRepository.existsById(id)) {
                return ResponseEntity.ok(ApiResponse.error("航班不存在"));
            }
            flightRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("删除成功", "航班已删除"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("删除失败: " + e.getMessage()));
        }
    }
}