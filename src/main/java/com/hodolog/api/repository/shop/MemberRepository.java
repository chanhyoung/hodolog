package com.hodolog.api.repository.shop;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hodolog.api.domain.shop.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByName(String name);
}
