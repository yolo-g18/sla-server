package com.g18.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "LONGTEXT")
	private String firstName;
	@Column(columnDefinition = "LONGTEXT")
	private String lastName;

	@Column(columnDefinition = "LONGTEXT")
	private String schoolName;
	@Column(columnDefinition = "LONGTEXT")
	private String job;
	@Column(columnDefinition = "LONGTEXT")
	private String major;
	@Column(columnDefinition = "LONGTEXT")
	private String avatar;
	@Column(columnDefinition = "LONGTEXT")
	private String bio;

	private Instant favourTimeFrom;
	private Instant favourTimeTo;

	@Column(columnDefinition = "LONGTEXT")
	private String email;
	@Column(columnDefinition = "LONGTEXT")
	private String address;

	@OneToMany(mappedBy = "user", orphanRemoval=true, cascade=CascadeType.ALL)
	private List<Notification> notis;

	@OneToMany(mappedBy = "reporter", orphanRemoval=true, cascade=CascadeType.ALL)
	private List<Report> reports;

	@OneToMany(mappedBy = "creator")
	private List<StudySet>  studySetsOwn; //list study sets created

	@OneToMany(mappedBy = "owner")
	private List<Room>  roomsOwn; //list rooms created

	@OneToMany(mappedBy = "owner")
	private List<Folder>  FoldersOwn; //list folders created

	@OneToMany(mappedBy = "member", orphanRemoval=true, cascade=CascadeType.ALL)
	private List<RoomMember>  MemberJoin; //list rooms joined

	@OneToMany(mappedBy = "user", orphanRemoval=true, cascade=CascadeType.ALL)
	private List<StudySetLearning>  studySetsLearning; //list study sets learning

	@OneToMany(mappedBy = "user", orphanRemoval=true, cascade = CascadeType.ALL)
	private List<CardLearning>  cardsLearning; //list cardLearning

	@OneToMany(mappedBy = "user", orphanRemoval=true, cascade = CascadeType.ALL)
	private List<RoomInvitation>  invitationList;

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				'}';
	}
}