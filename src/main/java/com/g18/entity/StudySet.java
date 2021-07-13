package com.g18.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudySet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id")
	private User creator;
	private String title;
	private String description;
	private String tag;
	private Instant createdDate;
	private boolean isPublic;

	@OneToMany(mappedBy = "studySet", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Card> cards;

}
