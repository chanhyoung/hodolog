package com.hodolog.api.service.shop;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hodolog.api.domain.shop.Member;
import com.hodolog.api.exception.shop.AlreadyExistsNameException;
import com.hodolog.api.exception.shop.ItemNotFound;
import com.hodolog.api.repository.shop.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);  // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new AlreadyExistsNameException();
                });
    }

    @Transactional
    public void updateMember(Long memberId, String name) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ItemNotFound("회원 정보를 찾을 수 없습니다. 회원 ID: " + memberId));
        member.setName(name);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ItemNotFound("회원 정보를 찾을 수 없습니다. 회원 ID: " + memberId));
    }
}
