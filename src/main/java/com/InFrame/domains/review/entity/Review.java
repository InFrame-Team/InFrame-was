package com.InFrame.domains.review.entity;

import com.InFrame.domains.reservation.entity.Reservation;
import com.InFrame.domains.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating; // 별점 (1 - 5)

    @Column(columnDefinition = "TEXT")
    private String comment; // 후기

    @Column
    private String reviewImageUrl; // 후기 사진 URL

    @Column(nullable = false)
    private LocalDateTime createdAt; // 작성 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Reservation reservation;


    @Builder
    public Review(int rating, String comment, String reviewImageUrl,
                  User user, Reservation reservation) {
        this.rating = rating;
        this.comment = comment;
        this.reviewImageUrl =reviewImageUrl;
        this.createdAt = LocalDateTime.now();
        this.user = user;
        this.reservation = reservation;
    }

}
