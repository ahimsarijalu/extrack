package com.ahimsarijalu.extrack.user;

import com.ahimsarijalu.extrack.expense.ExpenseUtil;
import org.springframework.beans.BeanUtils;

public class UserUtils {
    public static UserDTO mapUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setId(user.getId().toString());
        userDTO.setExpenses(user.getExpenses().stream().map(ExpenseUtil::mapExpenseToDTO).toList());
        return userDTO;
    }
}
