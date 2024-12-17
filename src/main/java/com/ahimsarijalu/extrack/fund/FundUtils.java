package com.ahimsarijalu.extrack.fund;

import com.ahimsarijalu.extrack.expense.ExpenseUtil;
import org.springframework.beans.BeanUtils;

public class FundUtils {
    public static FundDTO mapFundToDTO(Fund fund) {
        FundDTO fundDTO = new FundDTO();
        BeanUtils.copyProperties(fund, fundDTO);
        fundDTO.setId(fund.getId().toString());
        fundDTO.setUserId(fund.getUser().getId().toString());
        fundDTO.setExpenses(fund.getExpenses().stream().map(ExpenseUtil::mapExpenseToDTO).toList());
        return fundDTO;
    }

    public static FundDTO mapFundToDTOWithoutExpenses(Fund fund) {
        FundDTO fundDTO = new FundDTO();
        BeanUtils.copyProperties(fund, fundDTO);
        fundDTO.setId(fund.getId().toString());
        fundDTO.setUserId(fund.getUser().getId().toString());
//        fundDTO.setExpenses(fund.getExpenses().stream().map(ExpenseUtil::mapExpenseToDTO).toList());
        return fundDTO;
    }
}
