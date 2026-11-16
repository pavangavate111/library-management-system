package com.library.lms.service.impl;

import com.library.lms.entity.Member;
import com.library.lms.exception.DuplicateMemberEmailException;
import com.library.lms.exception.MemberNotFoundException;
import com.library.lms.repository.MemberRepository;
import com.library.lms.service.MemberService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import com.library.lms.repository.BorrowingRepository;
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BorrowingRepository borrowingRepository;

    public MemberServiceImpl(
            MemberRepository memberRepository,
            BorrowingRepository borrowingRepository) {

        this.memberRepository = memberRepository;
        this.borrowingRepository = borrowingRepository;
    }

    @Override
    public Member saveMember(Member member) {

        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalArgumentException(
                    "Member already exists with email: " + member.getEmail()
            );
        }

        return memberRepository.save(member);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }



    @Override
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(
                        "Member not found with ID: " + id
                ));
    }

    @Override
    public Member updateMember(Long id, Member member) {

        Member existingMember = getMemberById(id);

        if (memberRepository.existsByEmail(member.getEmail())
                && !existingMember.getEmail().equals(member.getEmail())) {

            throw new DuplicateMemberEmailException(
                    "Member already exists with email: " + member.getEmail()
            );
        }

        existingMember.setName(member.getName());
        existingMember.setEmail(member.getEmail());
        existingMember.setPhone(member.getPhone());
        existingMember.setAddress(member.getAddress());

        return memberRepository.save(existingMember);
    }

    @Override
    public void deleteMember(Long id) {

        Member member = getMemberById(id);

        long borrowingCount = borrowingRepository.countByMember_Id(id);

        if (borrowingCount > 0) {
            throw new IllegalArgumentException(
                    "Cannot delete this member because borrowing history exists."
            );
        }

        memberRepository.delete(member);
    }
    @Override
    public Page<Member> getAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @Override
    public List<Member> searchMembersByEmail(String email) {
        return memberRepository.findByEmailContainingIgnoreCase(email);
    }

    @Override
    public List<Member> searchMembersByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public long getTotalMembers() {
        return memberRepository.count();
    }
}