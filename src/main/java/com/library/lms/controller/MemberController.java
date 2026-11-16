package com.library.lms.controller;

import com.library.lms.dto.MemberRequest;
import com.library.lms.dto.MemberResponse;
import com.library.lms.entity.Member;
import com.library.lms.mapper.MemberMapper;
import com.library.lms.service.MemberService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    public MemberController(
            MemberService memberService,
            MemberMapper memberMapper) {

        this.memberService = memberService;
        this.memberMapper = memberMapper;
    }

    // Register member
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MemberResponse> saveMember(
            @Valid @RequestBody MemberRequest request) {

        Member member = memberMapper.toEntity(request);

        Member savedMember = memberService.saveMember(member);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberMapper.toResponse(savedMember));
    }

    // Get all members
    @GetMapping
    public ResponseEntity<Page<MemberResponse>> getAllMembers(
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {

        Page<MemberResponse> members = memberService.getAllMembers(pageable)
                .map(memberMapper::toResponse);

        return ResponseEntity.ok(members);
    }

    // Get member by ID
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(
            @PathVariable Long id) {

        Member member = memberService.getMemberById(id);

        return ResponseEntity.ok(memberMapper.toResponse(member));
    }

    // Update member
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequest request) {

        Member member = memberMapper.toEntity(request);

        Member updatedMember = memberService.updateMember(id, member);

        return ResponseEntity.ok(memberMapper.toResponse(updatedMember));
    }

    // Delete member
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {

        memberService.deleteMember(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<MemberResponse>> searchMembersByEmail(
            @RequestParam String email) {

        List<MemberResponse> members = memberService
                .searchMembersByEmail(email)
                .stream()
                .map(memberMapper::toResponse)
                .toList();

        return ResponseEntity.ok(members);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<MemberResponse>> searchMembersByName(
            @RequestParam String name) {

        List<MemberResponse> members = memberService
                .searchMembersByName(name)
                .stream()
                .map(memberMapper::toResponse)
                .toList();

        return ResponseEntity.ok(members);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalMembers() {
        return ResponseEntity.ok(memberService.getTotalMembers());
    }
}