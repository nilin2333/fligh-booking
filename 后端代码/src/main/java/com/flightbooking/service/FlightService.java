package com.flightbooking.service;

import java.util.List;

import com.flightbooking.dto.response.FlightResponse;

/**
 * 航班服务接口
 */
public interface FlightService {

    /**
     * 搜索航班
     * @param departureCity 出发城市
     * @param destinationCity 到达城市
     * @param departDate 出发日期
     * @param sortBy 排序方式（departureTime-按出发时间, duration-按经过时间, price-按票价）
     * @return 航班列表
     */
    List<FlightResponse> searchFlights(String departureCity, String destinationCity, 
                                       String departDate, String sortBy);
}