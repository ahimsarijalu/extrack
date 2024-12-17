package com.ahimsarijalu.extrack.user;

import com.ahimsarijalu.extrack.fund.FundUtils;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;

public class UserUtils {
    public static UserDTO mapUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setId(user.getId().toString());
        userDTO.setFunds(user.getFunds().stream().map(FundUtils::mapFundToDTOWithoutExpenses).collect(Collectors.toList()));
        return userDTO;
    }
}
