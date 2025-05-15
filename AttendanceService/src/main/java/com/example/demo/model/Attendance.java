package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Min(value = 1, message = "Employee ID must be greater than 0")
    private int employeeId;
    private LocalDate date;
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;
    private Long workHours;
}
