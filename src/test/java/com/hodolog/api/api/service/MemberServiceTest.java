package com.hodolog.api.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hodolog.api.domain.shop.Address;
import com.hodolog.api.domain.shop.Member;
import com.hodolog.api.exception.shop.AlreadyExistsNameException;
import com.hodolog.api.repository.shop.MemberRepository;
import com.hodolog.api.service.shop.MemberService;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;
  
    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입")
    void test1() {
      // given
      Member member = Member.builder()
        .name("홍길동")
        .address(new Address("서울시", "강남구", "123-456"))
        .build();
      
      // when
      Long savedId = memberService.join(member);

      // then
      assertEquals(1L, memberRepository.count());
      Member foundMember = memberRepository.findById(savedId).get();
      assertEquals("홍길동", foundMember.getName());
      assertEquals("서울시", foundMember.getAddress().getCity());
      assertEquals("강남구", foundMember.getAddress().getStreet());
    }

    @Test
    @DisplayName("중복 회원 예외발생")
    void test2() {
      // given
      Member member1 = Member.builder()
        .name("홍길동")
        .address(new Address("서울시", "강남구", "123-456"))
        .build();

      Member member2 = Member.builder()
        .name("홍길동")
        .address(new Address("서울시", "강남구", "123-456"))
        .build();

      // expected
      assertThrows(AlreadyExistsNameException.class, () -> {
        memberService.join(member1);
        memberService.join(member2); // 중복 회원 가입 시도
      });
    }
}
