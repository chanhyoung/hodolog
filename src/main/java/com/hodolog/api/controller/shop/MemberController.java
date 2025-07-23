package com.hodolog.api.controller.shop;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hodolog.api.domain.shop.Member;
import com.hodolog.api.service.shop.MemberService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/shop/members")
    public List<MemberResponse> members() {
        List<Member> findMembers = memberService.findAll();
        List<MemberResponse> responses = findMembers.stream()
                .map(member -> new MemberResponse(member.getId(), member.getName()))
                .collect(Collectors.toList());
        return responses;
    }
    
    @Data
    @AllArgsConstructor
    static class MemberResponse {
        private Long id;
        private String name;
    }

    @PostMapping("/shop/members")
    public CreateMemberResponse saveMember(@RequestBody @Valid CreateMemberRequest request) {
        log.info("request: {}", request);
        Member member = Member.builder()
                .name(request.getName())
                .build();

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/shop/members/{memberId}")
    public UpdateMemberResponse updateMember(
        @PathVariable Long memberId, 
        @RequestBody UpdateMemberRequest request) {
        memberService.updateMember(memberId, request.getName());
        Member member = memberService.findOne(memberId);
        return new UpdateMemberResponse(memberId, member.getName());
    }

    @Data
    static class UpdateMemberRequest {
        @NotEmpty
        private String name;
    }
    
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }
}













