package com.library.lms.mapper;

import com.library.lms.dto.MemberRequest;
import com.library.lms.dto.MemberResponse;
import com.library.lms.entity.Member;

import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    // Convert request DTO into entity
    public Member toEntity(MemberRequest request) {

        Member member = new Member();

        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setAddress(request.getAddress());

        return member;
    }

    // Convert entity into response DTO
    public MemberResponse toResponse(Member member) {

        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getAddress(),
                member.getCreatedAt()
        );
    }
}