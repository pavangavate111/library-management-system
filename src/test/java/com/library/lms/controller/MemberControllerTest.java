package com.library.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.lms.dto.MemberRequest;
import com.library.lms.dto.MemberResponse;
import com.library.lms.entity.Member;
import com.library.lms.exception.GlobalExceptionHandler;
import com.library.lms.exception.MemberNotFoundException;
import com.library.lms.mapper.MemberMapper;
import com.library.lms.service.MemberService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberController memberController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(memberController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(
                        new org.springframework.data.web.PageableHandlerMethodArgumentResolver()
                )
                .build();
    }

    @Test
    void shouldRegisterMemberSuccessfully() throws Exception {

        MemberRequest request = new MemberRequest();
        request.setName("Pavan");
        request.setEmail("pavan@gmail.com");
        request.setPhone("9876543210");
        request.setAddress("Hyderabad");

        Member member = new Member();
        Member savedMember = new Member();
        MemberResponse response = new MemberResponse();

        when(memberMapper.toEntity(any(MemberRequest.class)))
                .thenReturn(member);

        when(memberService.saveMember(member))
                .thenReturn(savedMember);

        when(memberMapper.toResponse(savedMember))
                .thenReturn(response);

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(memberService).saveMember(member);
    }

    @Test
    void shouldRejectMemberWhenRequiredFieldsAreMissing() throws Exception {

        MemberRequest request = new MemberRequest();

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(memberMapper, memberService);
    }

    @Test
    void shouldGetMemberByIdSuccessfully() throws Exception {

        Member member = new Member();
        MemberResponse response = new MemberResponse();

        when(memberService.getMemberById(1L))
                .thenReturn(member);

        when(memberMapper.toResponse(member))
                .thenReturn(response);

        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk());

        verify(memberService).getMemberById(1L);
    }


    @Test
    void shouldGetAllMembersSuccessfully() throws Exception {

        Member member = new Member();

        Page<Member> memberPage = new PageImpl<>(
                List.of(member),
                org.springframework.data.domain.PageRequest.of(0, 5),
                1
        );

        when(memberService.getAllMembers(any(Pageable.class)))
                .thenReturn(memberPage);

        when(memberMapper.toResponse(member))
                .thenReturn(new MemberResponse());

        mockMvc.perform(get("/api/members")
                        .param("page", "0")
                        .param("size", "5"))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        verify(memberService).getAllMembers(any(Pageable.class));
    }

    @Test
    void shouldSearchMembersByEmailSuccessfully() throws Exception {

        Member member = new Member();

        when(memberService.searchMembersByEmail("pavan@gmail.com"))
                .thenReturn(List.of(member));

        when(memberMapper.toResponse(member))
                .thenReturn(new MemberResponse());

        mockMvc.perform(get("/api/members/search")
                        .param("email", "pavan@gmail.com"))
                .andExpect(status().isOk());

        verify(memberService).searchMembersByEmail("pavan@gmail.com");
    }

    @Test
    void shouldSearchMembersByNameSuccessfully() throws Exception {

        Member member = new Member();

        when(memberService.searchMembersByName("Pavan"))
                .thenReturn(List.of(member));

        when(memberMapper.toResponse(member))
                .thenReturn(new MemberResponse());

        mockMvc.perform(get("/api/members/search/name")
                        .param("name", "Pavan"))
                .andExpect(status().isOk());

        verify(memberService).searchMembersByName("Pavan");
    }

    @Test
    void shouldGetTotalMembersSuccessfully() throws Exception {

        when(memberService.getTotalMembers())
                .thenReturn(10L);

        mockMvc.perform(get("/api/members/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(memberService).getTotalMembers();
    }

    @Test
    void shouldUpdateMemberSuccessfully() throws Exception {

        MemberRequest request = new MemberRequest();
        request.setName("Pavan Updated");
        request.setEmail("pavan@gmail.com");
        request.setPhone("9876543210");
        request.setAddress("Hyderabad");

        Member member = new Member();
        Member updatedMember = new Member();
        MemberResponse response = new MemberResponse();

        when(memberMapper.toEntity(any(MemberRequest.class)))
                .thenReturn(member);

        when(memberService.updateMember(1L, member))
                .thenReturn(updatedMember);

        when(memberMapper.toResponse(updatedMember))
                .thenReturn(response);

        mockMvc.perform(put("/api/members/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(memberService).updateMember(1L, member);
    }
    @Test
    void shouldDeleteMemberSuccessfully() throws Exception {

        doNothing().when(memberService).deleteMember(1L);

        mockMvc.perform(delete("/api/members/1"))
                .andExpect(status().isNoContent());

        verify(memberService).deleteMember(1L);
    }



    @Test
    void shouldReturn404WhenMemberNotFound() throws Exception {

        when(memberService.getMemberById(99L))
                .thenThrow(new MemberNotFoundException("Member not found"));

        mockMvc.perform(get("/api/members/99"))
                .andExpect(status().isNotFound());

        verify(memberService).getMemberById(99L);
    }

}