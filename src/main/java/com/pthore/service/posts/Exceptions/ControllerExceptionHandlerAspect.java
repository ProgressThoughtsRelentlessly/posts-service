package com.pthore.service.posts.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandlerAspect {
	
	
	@ExceptionHandler(value = {Exception.class})
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Exception occured for the request")
	@ResponseBody
	public String GenericExceptionHandler() {
		return "error";
	}
	
}
