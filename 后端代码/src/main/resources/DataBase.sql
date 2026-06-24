-- 创建数据库
CREATE DATABASE IF NOT EXISTS flight_booking_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE flight_booking_db;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status TINYINT DEFAULT 1 COMMENT '1: 启用, 0: 禁用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建索引
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);


-- 创建航班表
CREATE TABLE IF NOT EXISTS flights (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    flight_number VARCHAR(20) NOT NULL UNIQUE COMMENT '航班号',
    departure_city VARCHAR(50) NOT NULL COMMENT '出发城市',
    departure_code VARCHAR(10) COMMENT '出发机场代码',
    destination_city VARCHAR(50) NOT NULL COMMENT '到达城市',
    destination_code VARCHAR(10) COMMENT '到达机场代码',
    departure_time DATETIME NOT NULL COMMENT '出发时间',
    arrival_time DATETIME NOT NULL COMMENT '到达时间',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    cabin_type VARCHAR(20) NOT NULL COMMENT '舱位类型',
    available_seats INT NOT NULL COMMENT '可用座位数',
    total_seats INT NOT NULL COMMENT '总座位数',
    airline VARCHAR(50) NOT NULL COMMENT '航空公司',
    status TINYINT DEFAULT 1 COMMENT '1: 可用, 0: 不可用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建航班表索引
CREATE INDEX idx_flights_departure ON flights(departure_city);
CREATE INDEX idx_flights_destination ON flights(destination_city);
CREATE INDEX idx_flights_departure_time ON flights(departure_time);

-- 修改航班数据，使用基准日期2000-01-01存储（查询时会动态调整到用户选择的日期）
DELETE FROM flights;

INSERT INTO flights (flight_number, departure_city, departure_code, destination_city, 
                     destination_code, departure_time, arrival_time, price, cabin_type, 
                     available_seats, total_seats, airline, flight_type) VALUES
('CA1234', '北京', 'PEK', '上海', 'SHA', '2000-01-01 08:00:00', '2000-01-01 10:15:00', 399.00, 'economy', 150, 180, '中国国航', 'regular'),
('CA1235', '北京', 'PEK', '上海', 'SHA', '2000-01-01 14:30:00', '2000-01-01 16:45:00', 459.00, 'economy', 120, 180, '中国国航', 'regular'),
('MU5101', '北京', 'PEK', '上海', 'SHA', '2000-01-01 09:00:00', '2000-01-01 11:20:00', 429.00, 'economy', 140, 168, '东方航空', 'regular'), 
('MU5102', '北京', 'PEK', '上海', 'SHA', '2000-01-01 15:00:00', '2000-01-01 17:15:00', 529.00, 'business', 25, 30, '东方航空', 'regular'),
('CZ3501', '广州', 'CAN', '成都', 'CTU', '2000-01-01 07:30:00', '2000-01-01 10:30:00', 459.00, 'economy', 160, 186, '南方航空', 'regular'),
('CZ3502', '广州', 'CAN', '成都', 'CTU', '2000-01-01 13:00:00', '2000-01-01 16:00:00', 519.00, 'economy', 130, 186, '南方航空', 'regular'),
('ZH9401', '深圳', 'SZX', '杭州', 'HGH', '2000-01-01 08:30:00', '2000-01-01 10:45:00', 529.00, 'economy', 145, 178, '深圳航空', 'regular'),
('ZH9402', '深圳', 'SZX', '杭州', 'HGH', '2000-01-01 16:00:00', '2000-01-01 18:15:00', 589.00, 'business', 20, 28, '深圳航空', 'regular'),
('HU7801', '西安', 'XIY', '重庆', 'CKG', '2000-01-01 07:00:00', '2000-01-01 08:30:00', 329.00, 'economy', 156, 180, '海南航空', 'regular'),
('HU7802', '西安', 'XIY', '重庆', 'CKG', '2000-01-01 11:30:00', '2000-01-01 13:00:00', 369.00, 'economy', 140, 180, '海南航空', 'regular'),
('CA1301', '北京', 'PEK', '广州', 'CAN', '2000-01-01 07:00:00', '2000-01-01 10:20:00', 699.00, 'economy', 170, 198, '中国国航', 'regular'),
('MU5301', '上海', 'SHA', '广州', 'CAN', '2000-01-01 08:00:00', '2000-01-01 11:15:00', 659.00, 'economy', 165, 186, '东方航空', 'regular');


-- 添加航班类型字段
ALTER TABLE flights ADD COLUMN flight_type VARCHAR(20) NOT NULL DEFAULT 'regular';

-- 更新现有航班为固定航班
UPDATE flights SET flight_type = 'regular';

-- 插入特殊航班（特定日期才有）
INSERT INTO flights (flight_number, departure_city, departure_code, destination_city, 
                     destination_code, departure_time, arrival_time, price, cabin_type, 
                     available_seats, total_seats, airline, flight_type) VALUES
('CA9999', '北京', 'PEK', '上海', 'SHA', '2026-05-16 06:00:00', '2026-05-16 08:15:00', 299.00, 'economy', 180, 200, '中国国航', 'special'),
('MU8888', '北京', 'PEK', '上海', 'SHA', '2026-05-16 20:00:00', '2026-05-16 22:15:00', 349.00, 'economy', 160, 180, '东方航空', 'special'),
('CZ7777', '广州', 'CAN', '成都', 'CTU', '2026-05-16 09:00:00', '2026-05-16 12:00:00', 399.00, 'economy', 150, 180, '南方航空', 'special'),
('CA9998', '北京', 'PEK', '上海', 'SHA', '2026-05-17 07:00:00', '2026-05-17 09:15:00', 329.00, 'economy', 170, 190, '中国国航', 'special'),
('HU6666', '西安', 'XIY', '重庆', 'CKG', '2026-05-17 15:00:00', '2026-05-17 16:30:00', 289.00, 'economy', 160, 180, '海南航空', 'special');



-- 创建管理员表
CREATE TABLE IF NOT EXISTS admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100),
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    real_name VARCHAR(50),
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status TINYINT DEFAULT 1 COMMENT '1: 启用, 0: 禁用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建索引
CREATE INDEX idx_admins_username ON admins(username);
CREATE INDEX idx_admins_email ON admins(email);

-- 插入初始管理员账号（密码为 admin123，已加密）
INSERT INTO admins (username, email, password, phone, real_name, role) VALUES
('admin', 'admin@flight.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '13800138000', '系统管理员', 'ADMIN');