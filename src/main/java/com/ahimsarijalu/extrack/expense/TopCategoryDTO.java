package com.ahimsarijalu.extrack.expense;

import lombok.Data;

@Data
public class TopCategoryDTO {
    private String topCategory;
    private Long totalAmount;

    public TopCategoryDTO(String topCategory, Long totalAmount) {
        this.topCategory = topCategory;
        this.totalAmount = totalAmount;
    }
}
