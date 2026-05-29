package com.syncall.api.infra.exception;

import com.syncall.api.dto.error.ErrorResponseDTO;
import com.syncall.api.exception.BusinessException;
import com.syncall.api.exception.ResourceNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<ErrorResponseDTO> handleResourceNotFound(Exception e, WebRequest webRequest){
        String path = webRequest.getDescription(false).replace("uri=", "");
        var errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.toString(), e.getMessage(), path);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<ErrorResponseDTO> handleBusinessException(Exception e, WebRequest webRequest){
        String path = webRequest.getDescription(false).replace("uri=", "");
        var errorResponse = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.toString(), e.getMessage(), path);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest webRequest) {
        List<String> fieldError = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();

        String path = webRequest.getDescription(false).replace("uri=", "");
        var errorResponse = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.toString(), fieldError, path);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
