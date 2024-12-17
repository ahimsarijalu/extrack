package com.ahimsarijalu.extrack.fund;

import com.ahimsarijalu.extrack.expense.ExpenseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class FundDTO {
    private String id;

    @NotBlank(message = "Fund name should not be empty or null")
    private String name;

    @NotNull(message = "Balance should not be null")
    private Long balance;

    private String userId;

    private List<ExpenseDTO> expenses;
}
