
package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.enitity.RenterApplicationEnitiy;

@Repository
public interface RenterApplicationRepository extends JpaRepository<RenterApplicationEnitiy, Long> {
    
    // Spring will automatically implement this method for you!
    List<RenterApplicationEnitiy> findByStatus(String status);
}