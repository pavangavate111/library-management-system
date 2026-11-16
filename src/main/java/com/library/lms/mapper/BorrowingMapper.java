package com.library.lms.mapper;

import com.library.lms.dto.BorrowingResponse;
import com.library.lms.entity.Borrowing;
import com.library.lms.service.FineCalculator;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class BorrowingMapper {

    private final FineCalculator fineCalculator;
    public BorrowingMapper(FineCalculator fineCalculator) {
        this.fineCalculator = fineCalculator;
    }

    public BorrowingResponse toResponse(Borrowing borrowing) {

        return new BorrowingResponse(
                borrowing.getId(),
                borrowing.getBook().getId(),
                borrowing.getBook().getTitle(),
                borrowing.getMember().getId(),
                borrowing.getMember().getName(),
                borrowing.getBorrowDate(),
                borrowing.getDueDate(),
                borrowing.getReturnDate(),
                borrowing.getStatus(),
                fineCalculator.calculate(borrowing)
        );
    }
}