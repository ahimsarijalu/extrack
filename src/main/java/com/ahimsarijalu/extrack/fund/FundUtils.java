package com.ahimsarijalu.extrack.fund;

import org.springframework.beans.BeanUtils;

public class FundUtils {
    public static FundDTO mapFundToDTO(Fund fund) {
        FundDTO fundDTO = new FundDTO();
        BeanUtils.copyProperties(fund, fundDTO);
        fundDTO.setId(fund.getId().toString());
        fundDTO.setUserId(fund.getUser().getId().toString());
        return fundDTO;
    }
}
