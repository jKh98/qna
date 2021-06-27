package com.jkh98.qna.handler;

import com.jkh98.qna.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//    @ExceptionHandler(value = {Exception.class})
//    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("message", "Server Error");
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
//    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "Resource not found.");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }

}
