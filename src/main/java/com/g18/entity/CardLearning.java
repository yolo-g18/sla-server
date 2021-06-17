package com.g18.entity;

import com.g18.model.Color;
import com.g18.model.Status;
import com.g18.model.UserCardId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CardLearning {

    @EmbeddedId
    private UserCardId userCardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    @MapsId("cardId")
    private Card card;


    @Max(5)
    @Min(0)
    private int q; //quality
    private double intervalTime;
    private double eFactor;

    private String hint;
    private Instant LearnedDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Color color;

}
