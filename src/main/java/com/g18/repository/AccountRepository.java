package com.g18.repository;

import com.g18.entity.Account;
import com.g18.entity.StudySet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    List<Account> findByUsernameContains(String keyword);
}
