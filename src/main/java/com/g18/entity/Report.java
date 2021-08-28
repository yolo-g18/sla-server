package com.g18.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="reporter_id")

    private User reporter;

    @ManyToOne
    @JoinColumn(name="study_set_id")
    private StudySet studySet;

    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @JsonProperty
    private boolean isChecked;

    private Instant createdTime;
}
