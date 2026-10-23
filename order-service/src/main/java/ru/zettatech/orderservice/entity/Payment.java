package ru.zettatech.orderservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "yookassa_payment_id", unique = true)
    private String yookassaPaymentId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String status; // PENDING, PAID, CANCELLED, REFUNDED

    @Column(name = "confirmation_url")
    private String confirmationUrl;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    public Payment() {
    }

    public Payment(Long orderId, String yookassaPaymentId, BigDecimal amount, String currency, String status) {
        this.orderId = orderId;
        this.yookassaPaymentId = yookassaPaymentId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }
}
