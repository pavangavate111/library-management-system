
package com.library.lms.mapper;

import com.library.lms.dto.BookRequest;
import com.library.lms.entity.Book;
import org.springframework.stereotype.Component;
import com.library.lms.dto.BookResponse;

@Component
public class BookMapper {

    //while storing
    public Book toEntity(BookRequest request) {

        Book book = new Book();

        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());
        book.setPublisher(request.getPublisher());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getAvailableCopies());

        return book;
    }

    //for put method request to entity updation
    public Book updateEntity(BookRequest request, Book book) {

        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());
        book.setPublisher(request.getPublisher());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getAvailableCopies());

//        book.setVersion(request.getVersion());
        return book;
    }


//    while retriving
    public BookResponse toResponse(Book book) {

        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getPublisher(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.getCreatedAt(),
                book.getVersion()
        );
    }


}
