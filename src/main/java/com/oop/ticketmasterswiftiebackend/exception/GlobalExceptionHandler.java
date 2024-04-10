package com.oop.ticketmasterswiftiebackend.exception;

import com.oop.ticketmasterswiftiebackend.common.constants.CommonError;
import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler {

    private static List<String> getErrorMessages(WebExchangeBindException ex) {
        List<String> errorMessages = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String errorMessage = MessageFormat.format("{0}: {1}", error.getField(), error.getDefaultMessage());
            errorMessages.add(errorMessage);
        }
        return errorMessages;
    }

    // General exception handler
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Exception occurred: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(CommonError.INTERNAL_SERVER_ERROR.getCode(), CommonError.INTERNAL_SERVER_ERROR.getBusinessCode(), CommonError.INTERNAL_SERVER_ERROR.getDescription(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handles the exceptions thrown by the ResponseStatus annotation
    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) throws MethodArgumentNotValidException {
        log.error("Exception occurred under ResponseStatusException.class: ", ex.getCause());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(ex.getStatusCode().value())
                .businessCode(CommonError.BAD_REQUEST.getBusinessCode())
                .message(ex.getReason())
                .build();

        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String errorMessage = MessageFormat.format("{0}: {1}", error.getField(), error.getDefaultMessage());
            errorMessages.add(errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(CommonError.BAD_REQUEST.getCode());
        errorResponse.setBusinessCode(CommonError.BAD_REQUEST.getBusinessCode());
        errorResponse.setMessage(CommonError.BAD_REQUEST.getDescription());
        errorResponse.setErrors(errorMessages);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(WebExchangeBindException ex) {
        // Create a list to store the validation error messages
        List<String> errorMessages = getErrorMessages(ex);

        // Create the custom error response object
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(CommonError.BAD_REQUEST.getCode());
        errorResponse.setBusinessCode(CommonError.BAD_REQUEST.getBusinessCode());
        errorResponse.setMessage(CommonError.BAD_REQUEST.getDescription());
        errorResponse.setErrors(errorMessages);

        // Return the customized error response with the appropriate status code
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatusCode(), ex.getBusinessCode(), ex.getDescription(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = ex.getMessage().split(":")[0];
        ErrorResponse errorResponse = new ErrorResponse(CommonError.BAD_REQUEST.getCode(), CommonError.BAD_REQUEST.getBusinessCode(), CommonError.BAD_REQUEST.getDescription(), List.of(message));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(CommonError.NOT_FOUND.getCode(), CommonError.NOT_FOUND.getBusinessCode(), CommonError.NOT_FOUND.getDescription(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
