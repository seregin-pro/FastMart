package ru.zettatech.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zettatech.orderservice.entity.Payment;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByYookassaPaymentId(String yookassaPaymentId);
    Optional<Payment> findByOrderId(Long orderId);
}
