package com.library.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.lms.exception.GlobalExceptionHandler;
import com.library.lms.service.BorrowingService;
import com.library.lms.mapper.BorrowingMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.library.lms.dto.BorrowingRequest;
import com.library.lms.dto.BorrowingResponse;
import com.library.lms.entity.Borrowing;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class BorrowingControllerTest {

    @Mock
    private BorrowingService borrowingService;

    @Mock
    private BorrowingMapper borrowingMapper;

    @InjectMocks
    private BorrowingController borrowingController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(borrowingController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldBorrowBookSuccessfully() throws Exception {

        BorrowingRequest request = new BorrowingRequest();
        request.setBookId(1L);
        request.setMemberId(1L);
        Borrowing borrowing = mock(Borrowing.class);
        BorrowingResponse response = mock(BorrowingResponse.class);
        when(borrowingService.borrowBook(1L, 1L))
                .thenReturn(borrowing);

        when(borrowingMapper.toResponse(borrowing))
                .thenReturn(response);

        mockMvc.perform(post("/api/borrowings/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(borrowingService).borrowBook(1L, 1L);
    }


    @Test
    void shouldReturnBookSuccessfully() throws Exception {

        Borrowing borrowing = mock(Borrowing.class);
        BorrowingResponse response = mock(BorrowingResponse.class);

        when(borrowingService.returnBook(1L))
                .thenReturn(borrowing);

        when(borrowingMapper.toResponse(borrowing))
                .thenReturn(response);

        mockMvc.perform(put("/api/borrowings/return/1"))
                .andExpect(status().isOk());

        verify(borrowingService).returnBook(1L);
    }

    @Test
    void shouldGetBorrowingHistoryByMember() throws Exception {

        List<Borrowing> borrowings = List.of(
                mock(Borrowing.class)
        );

        when(borrowingService.getBorrowingHistory(1L))
                .thenReturn(borrowings);

        when(borrowingMapper.toResponse(any(Borrowing.class)))
                .thenReturn(mock(BorrowingResponse.class));

        mockMvc.perform(get("/api/borrowings/member/1/history"))
                .andExpect(status().isOk());

        verify(borrowingService).getBorrowingHistory(1L);
    }

    @Test
    void shouldGetAllActiveBorrowings() throws Exception {

        List<Borrowing> borrowings = List.of(
                mock(Borrowing.class)
        );

        when(borrowingService.getActiveBorrowings())
                .thenReturn(borrowings);

        when(borrowingMapper.toResponse(any(Borrowing.class)))
                .thenReturn(mock(BorrowingResponse.class));

        mockMvc.perform(get("/api/borrowings/active"))
                .andExpect(status().isOk());

        verify(borrowingService).getActiveBorrowings();
    }

    @Test
    void shouldGetAllOverdueBorrowings() throws Exception {

        List<Borrowing> borrowings = List.of(
                mock(Borrowing.class)
        );

        when(borrowingService.getOverdueBorrowings())
                .thenReturn(borrowings);

        when(borrowingMapper.toResponse(any(Borrowing.class)))
                .thenReturn(mock(BorrowingResponse.class));

        mockMvc.perform(get("/api/borrowings/overdue"))
                .andExpect(status().isOk());

        verify(borrowingService).getOverdueBorrowings();
    }

    @Test
    void shouldUpdateOverdueBorrowings() throws Exception {

        doNothing().when(borrowingService).updateOverdueBorrowings();

        mockMvc.perform(put("/api/borrowings/update-overdue"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Overdue borrowing statuses updated successfully."
                ));

        verify(borrowingService).updateOverdueBorrowings();
    }

    @Test
    void shouldGetTotalBorrowedBooks() throws Exception {

        when(borrowingService.getTotalBorrowedBooks())
                .thenReturn(5L);

        mockMvc.perform(get("/api/borrowings/count/borrowed"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(borrowingService).getTotalBorrowedBooks();
    }


    @Test
    void shouldGetTotalReturnedBooks() throws Exception {

        when(borrowingService.getTotalReturnedBooks())
                .thenReturn(3L);

        mockMvc.perform(get("/api/borrowings/count/returned"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        verify(borrowingService).getTotalReturnedBooks();
    }

    @Test
    void shouldGetTotalOverdueBooks() throws Exception {

        when(borrowingService.getTotalOverdueBooks())
                .thenReturn(2L);

        mockMvc.perform(get("/api/borrowings/count/overdue"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        verify(borrowingService).getTotalOverdueBooks();
    }

    @Test
    void shouldGetTotalBorrowings() throws Exception {

        when(borrowingService.getTotalBorrowings())
                .thenReturn(10L);

        mockMvc.perform(get("/api/borrowings/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(borrowingService).getTotalBorrowings();
    }


    @Test
    void shouldGetBorrowingHistoryByBook() throws Exception {

        List<Borrowing> borrowings = List.of(
                mock(Borrowing.class)
        );

        when(borrowingService.getBorrowingHistoryByBook(1L))
                .thenReturn(borrowings);

        when(borrowingMapper.toResponse(any(Borrowing.class)))
                .thenReturn(mock(BorrowingResponse.class));

        mockMvc.perform(get("/api/borrowings/book/1/history"))
                .andExpect(status().isOk());

        verify(borrowingService).getBorrowingHistoryByBook(1L);
    }

    @Test
    void shouldGetTotalBorrowingsByMember() throws Exception {

        when(borrowingService.getTotalBorrowingsByMember(1L))
                .thenReturn(4L);

        mockMvc.perform(get("/api/borrowings/member/1/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("4"));

        verify(borrowingService).getTotalBorrowingsByMember(1L);
    }

    @Test
    void shouldGetActiveBorrowingsCountByMember() throws Exception {

        when(borrowingService.getActiveBorrowingsByMember(1L))
                .thenReturn(2L);

        mockMvc.perform(get("/api/borrowings/member/1/active-count"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        verify(borrowingService).getActiveBorrowingsByMember(1L);
    }


    @Test
    void shouldGetReturnedBorrowingsByMember() throws Exception {

        List<Borrowing> borrowings = List.of(
                mock(Borrowing.class)
        );

        when(borrowingService.getReturnedBorrowingsByMember(1L))
                .thenReturn(borrowings);

        when(borrowingMapper.toResponse(any(Borrowing.class)))
                .thenReturn(mock(BorrowingResponse.class));

        mockMvc.perform(get("/api/borrowings/member/1/returned"))
                .andExpect(status().isOk());

        verify(borrowingService).getReturnedBorrowingsByMember(1L);
    }
    @Test
    void shouldGetOverdueBorrowingsByMember() throws Exception {

        List<Borrowing> borrowings = List.of(
                mock(Borrowing.class)
        );

        when(borrowingService.getOverdueBorrowingsByMember(1L))
                .thenReturn(borrowings);

        when(borrowingMapper.toResponse(any(Borrowing.class)))
                .thenReturn(mock(BorrowingResponse.class));

        mockMvc.perform(get("/api/borrowings/member/1/overdue"))
                .andExpect(status().isOk());

        verify(borrowingService).getOverdueBorrowingsByMember(1L);
    }

    @Test
    void shouldGetBorrowingsBetweenDates() throws Exception {

        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 31);

        List<Borrowing> borrowings = List.of(
                mock(Borrowing.class)
        );

        when(borrowingService.getBorrowingsBetweenDates(startDate, endDate))
                .thenReturn(borrowings);

        when(borrowingMapper.toResponse(any(Borrowing.class)))
                .thenReturn(mock(BorrowingResponse.class));

        mockMvc.perform(get("/api/borrowings/date-range")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-01-31"))
                .andExpect(status().isOk());

        verify(borrowingService).getBorrowingsBetweenDates(startDate, endDate);
    }

    @Test
    void shouldGetTotalBorrowingsBetweenDates() throws Exception {

        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 31);

        when(borrowingService.getTotalBorrowingsBetweenDates(startDate, endDate))
                .thenReturn(5L);

        mockMvc.perform(get("/api/borrowings/date-range/count")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-01-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(borrowingService)
                .getTotalBorrowingsBetweenDates(startDate, endDate);
    }

    @Test
    void shouldGetReturnedBorrowingsBetweenDates() throws Exception {

        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 31);

        List<Borrowing> borrowings = List.of(
                mock(Borrowing.class)
        );

        when(borrowingService.getReturnedBorrowingsBetweenDates(startDate, endDate))
                .thenReturn(borrowings);

        when(borrowingMapper.toResponse(any(Borrowing.class)))
                .thenReturn(mock(BorrowingResponse.class));

        mockMvc.perform(get("/api/borrowings/returned/date-range")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-01-31"))
                .andExpect(status().isOk());

        verify(borrowingService)
                .getReturnedBorrowingsBetweenDates(startDate, endDate);
    }

    @Test
    void shouldGetTotalReturnedBorrowingsBetweenDates() throws Exception {

        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 31);

        when(borrowingService.getTotalReturnedBorrowingsBetweenDates(startDate, endDate))
                .thenReturn(3L);

        mockMvc.perform(get("/api/borrowings/returned/date-range/count")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-01-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        verify(borrowingService)
                .getTotalReturnedBorrowingsBetweenDates(startDate, endDate);
    }

    @Test
    void shouldCalculateFineByBorrowingId() throws Exception {

        when(borrowingService.calculateFine(1L))
                .thenReturn(new BigDecimal("25.00"));

        mockMvc.perform(get("/api/borrowings/1/fine"))
                .andExpect(status().isOk())
                .andExpect(content().string("25.00"));

        verify(borrowingService).calculateFine(1L);
    }

    @Test
    void shouldGetTotalFineByMember() throws Exception {

        when(borrowingService.getTotalFineByMember(1L))
                .thenReturn(new BigDecimal("50.00"));

        mockMvc.perform(get("/api/borrowings/member/1/total-fine"))
                .andExpect(status().isOk())
                .andExpect(content().string("50.00"));

        verify(borrowingService).getTotalFineByMember(1L);
    }

    @Test
    void shouldGetTotalLibraryFine() throws Exception {

        when(borrowingService.getTotalLibraryFine())
                .thenReturn(new BigDecimal("150.00"));

        mockMvc.perform(get("/api/borrowings/total-fine"))
                .andExpect(status().isOk())
                .andExpect(content().string("150.00"));

        verify(borrowingService).getTotalLibraryFine();
    }

    @Test
    void shouldRejectBorrowingRequestWithMissingFields() throws Exception {

        BorrowingRequest request = new BorrowingRequest();

        mockMvc.perform(post("/api/borrowings/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(borrowingService);
    }
    @Test
    void shouldRejectInvalidDateRange() throws Exception {

        mockMvc.perform(get("/api/borrowings/date-range")
                        .param("startDate", "invalid-date")
                        .param("endDate", "2026-01-31"))
                .andExpect(status().isBadRequest());
//        .andExpect(status().isConflict());
    }

    @Test
    void shouldRejectMissingDateRangeParameters() throws Exception {

        mockMvc.perform(get("/api/borrowings/date-range"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectStartDateAfterEndDate() throws Exception {

        mockMvc.perform(get("/api/borrowings/date-range")
                        .param("startDate", "2026-02-01")
                        .param("endDate", "2026-01-01"))
                .andExpect(status().isBadRequest());
    }


}


