package com.hodolog.api.repository;

import java.util.Optional;

import com.hodolog.api.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByEmailAndPassword(String email, String password);
	Optional<Users> findByEmail(String email);
}
