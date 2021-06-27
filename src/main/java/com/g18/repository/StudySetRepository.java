package com.g18.repository;

import com.g18.entity.Folder;
import com.g18.entity.StudySet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudySetRepository extends JpaRepository<StudySet, Long> {
}
