package com.ahimsarijalu.extrack.expense;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpenseDTO {
    private String id;
    private String title;
    private String description;
    private Long amount;
    private Category category;
    private LocalDateTime expenseDate;
    private String userId;
    private String fundId;
}
