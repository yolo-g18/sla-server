package com.g18.repository;

import com.g18.entity.Account;

import com.g18.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    List<Account> findByUsernameContains(String keyword);

    @Query(value = "SELECT username FROM account where user_id = ?1 ", nativeQuery = true)
    String findUserNameByUserId(Long userId);
    Optional<Account> findByUser(User user);

    Page<Account> findByUsernameContains(String userName, Pageable pageable);

    List<Account> findByUsernameContaining(String username);

}
