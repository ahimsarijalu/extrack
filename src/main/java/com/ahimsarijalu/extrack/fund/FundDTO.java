package com.ahimsarijalu.extrack.fund;

import com.ahimsarijalu.extrack.expense.ExpenseDTO;
import lombok.Data;

import java.util.List;

@Data
public class FundDTO {
    private String id;
    private String name;
    private Long balance;
    private String userId;
    private List<ExpenseDTO> expenses;
}
