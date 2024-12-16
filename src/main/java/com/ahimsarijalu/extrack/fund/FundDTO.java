package com.ahimsarijalu.extrack.fund;

import lombok.Data;

@Data
public class FundDTO {
    private String id;
    private String name;
    private Long balance;
    private String userId;
}
