package com.g18.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;
	private String lastName;

	private Date dateOfBirth;
	private Boolean gender;
	private String schoolName;
	private String job;
	private String phone;
	private String avatar;

	private Instant favourTimeFrom;
	private Instant favourTimeTo;
	private String email;
	private String address;

	@OneToMany(mappedBy = "creator")
	private List<StudySet>  studySetsOwn; //list study sets created

	@OneToMany(mappedBy = "owner")
	private List<Room>  roomsOwn; //list rooms created

	@OneToMany(mappedBy = "owner")
	private List<Folder>  FoldersOwn; //list folders created

	@OneToMany(mappedBy = "room")
	private List<RoomMember>  roomsJoin; //list rooms joined

	@OneToMany(mappedBy = "studySet")
	private List<StudySetLearning>  studySetsLearning; //list study sets learning

//	@OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
//	private List<CardLearning>  cardsLearning;

}