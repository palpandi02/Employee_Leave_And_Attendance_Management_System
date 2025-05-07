package com.example.demo.repository;
 
import com.example.demo.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
 
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    Optional<Shift> findByEmployeeId(int employeeId);
    List<Shift> findBySwapRequestedTrue();
}