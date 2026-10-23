package ru.zettatech.orderservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "order_status_history")
@Getter
@Setter
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "previous_status")
    private String previousStatus;

    @Column(name = "new_status", nullable = false)
    private String newStatus;

    @Column(name = "changed_by")
    private String changedBy; // user, system, payment_gateway

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    public OrderStatusHistory() {
    }

    public OrderStatusHistory(Long orderId, String previousStatus, String newStatus, String changedBy, String comment) {
        this.orderId = orderId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.comment = comment;
    }
}
