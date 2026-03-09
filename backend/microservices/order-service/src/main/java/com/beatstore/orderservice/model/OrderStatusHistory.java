package com.beatstore.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_history")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusHistory {

    @Id
    @SequenceGenerator(
            name = "order_status_history_id_seq",
            sequenceName = "order_status_history_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_status_history_id_seq"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private Order order;

    private String oldStatus;

    @Column(nullable = false)
    private String newStatus;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}