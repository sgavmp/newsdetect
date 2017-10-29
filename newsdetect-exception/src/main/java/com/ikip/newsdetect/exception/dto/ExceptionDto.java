package com.ikip.newsdetect.exception.dto;

import com.ikip.newsdetect.exception.Errors;
import lombok.Data;

@Data
public class ExceptionDto {
    private Errors code;
    private String description;
    private String field;
    private String value;
}
