
package com.library.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.lms.dto.BookRequest;
import com.library.lms.dto.BookResponse;
import com.library.lms.entity.Book;
import com.library.lms.exception.GlobalExceptionHandler;
import com.library.lms.mapper.BookMapper;
import com.library.lms.service.BookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import com.library.lms.exception.BookNotFoundException;


@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @Test
    void shouldAddBookSuccessfully() throws Exception {

        BookRequest request = new BookRequest();

        request.setTitle("Java Programming");
        request.setAuthor("James Gosling");
        request.setTotalCopies(10);
        request.setAvailableCopies(10);


        Book book = new Book();
        Book savedBook = new Book();

        BookResponse response = new BookResponse();

        when(bookMapper.toEntity(any(BookRequest.class)))
                .thenReturn(book);

        when(bookService.saveBook(book))
                .thenReturn(savedBook);

        when(bookMapper.toResponse(savedBook))
                .thenReturn(response);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(bookService).saveBook(book);
    }


    @Test
    void shouldRejectBookWhenRequiredFieldsAreMissing() throws Exception {

        BookRequest request = new BookRequest();

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookMapper, bookService);
    }

    @Test
    void shouldGetBookByIdSuccessfully() throws Exception {

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Java Programming");

        BookResponse response = new BookResponse();

        when(bookService.getBookById(1L))
                .thenReturn(book);

        when(bookMapper.toResponse(book))
                .thenReturn(response);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk());

        verify(bookService).getBookById(1L);
    }

    @Test
    void shouldReturn404WhenBookNotFound() throws Exception {

        when(bookService.getBookById(99L))
                .thenThrow(new BookNotFoundException("Book not found"));

        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound());

        verify(bookService).getBookById(99L);
    }

}
