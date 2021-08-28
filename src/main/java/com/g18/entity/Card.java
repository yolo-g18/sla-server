package com.g18.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "study_set_id")
	private StudySet studySet;

	@Column(columnDefinition = "LONGTEXT")
	private String front;
	@Column(columnDefinition = "LONGTEXT")
	private String back;

	@OneToMany(mappedBy = "card", orphanRemoval=true, cascade=CascadeType.ALL)
	private List<CardLearning> cardLearningList; //list cardLearning
}
