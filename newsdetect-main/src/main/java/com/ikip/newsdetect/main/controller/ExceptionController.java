package com.ikip.newsdetect.main.controller;

import com.ikip.newsdetect.exception.Errors;
import com.ikip.newsdetect.exception.dto.ExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RestController
@Slf4j
public class ExceptionController {

	@ExceptionHandler(value = Exception.class)
    public ExceptionDto defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("Catch error",e);
        ExceptionDto dto = new ExceptionDto();
        dto.setCode(Errors.SERVER_ERROR);
        dto.setDescription(e.getMessage());
        return dto;
    }
	
}
