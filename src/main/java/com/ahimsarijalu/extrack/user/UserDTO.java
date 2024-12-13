package com.ahimsarijalu.extrack.user;

import com.ahimsarijalu.extrack.expense.ExpenseDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private Integer age;
    private List<ExpenseDTO> expenses;
}
