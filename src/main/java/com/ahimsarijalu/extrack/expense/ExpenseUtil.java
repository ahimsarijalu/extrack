package com.ahimsarijalu.extrack.expense;

import org.springframework.beans.BeanUtils;

public class ExpenseUtil {
    public static ExpenseDTO mapExpenseToDTO(Expense expense) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        BeanUtils.copyProperties(expense, expenseDTO);
        expenseDTO.setId(expense.getId().toString());
        expenseDTO.setUserId(expense.getUser().getId().toString());
        expenseDTO.setFundId(expense.getFund().getId().toString());
        return expenseDTO;
    }
}
