package com.ahimsarijalu.extrack.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private Integer status;
    private Boolean success;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
}
