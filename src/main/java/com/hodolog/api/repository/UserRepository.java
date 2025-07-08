package com.hodolog.api.repository;

import java.util.Optional;

import com.hodolog.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmailAndPassword(String email, String password);
	Optional<User> findByEmail(String email);
}
