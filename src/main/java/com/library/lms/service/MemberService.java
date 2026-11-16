package com.library.lms.service;

import com.library.lms.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {

    Member saveMember(Member member);

    List<Member> getAllMembers();

    Member getMemberById(Long id);

    Member updateMember(Long id, Member member);

    void deleteMember(Long id);

    Page<Member> getAllMembers(Pageable pageable);

    List<Member> searchMembersByEmail(String email);

    List<Member> searchMembersByName(String name);

    long getTotalMembers();


}