package com.example.demo.repository;

import com.example.demo.enitity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Explicitly joins Booking -> RentalProduct (product) -> Renter (owner) -> keycloakId
    @Query("SELECT b FROM Booking b WHERE b.product.owner.keycloakId = :ownerId")
    List<Booking> findByProductOwnerId(@Param("ownerId") String ownerId);

    List<Booking> findByUserId(String userId);
}