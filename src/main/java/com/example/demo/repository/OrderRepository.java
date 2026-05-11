package com.example.demo.repository;



import com.example.demo.enitity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

   // It finds the order using the unique reference code sent to Chapa.
   Optional<Order> findByTransactionReference(String transactionReference);
}
