package com.g18.entity;

import com.g18.model.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Folder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id")
	private User owner;

	private String title;
	private String description;
	private Instant createdDate;
	private Instant updateDate;

	@Enumerated(EnumType.STRING)
	private Color color;

	@OneToMany(mappedBy = "folder",orphanRemoval=true, cascade=CascadeType.ALL)
	private List<FolderStudySet> folderStudySets; //list study sets in folder

	@Override
	public String toString() {
		return "Folder{" +
				"id=" + id +
				'}';
	}
}