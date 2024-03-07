package edu.bbte.idde.keim2152.spring.controller;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import edu.bbte.idde.keim2152.spring.model.dto.outgoing.ResponseMessageDto;
import edu.bbte.idde.keim2152.spring.service.exception.ServiceEntityNotFoundException;
import edu.bbte.idde.keim2152.spring.service.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ServiceErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ServiceEntityNotFoundException.class)
    public final ResponseMessageDto handleServiceEntityNotFound(ServiceEntityNotFoundException e) {
        log.debug("ServiceEntityNotFoundException", e);
        return new ResponseMessageDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceException.class)
    public final ResponseMessageDto handleServiceException(ServiceException e) {
        log.debug("ServiceException", e);
        return new ResponseMessageDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseMessageDto handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.debug("HttpMessageNotReadableException", e);
        return new ResponseMessageDto("Request body missing or malformed");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseMessageDto handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException e) {
        log.debug("MethodArgumentTypeMismatchException", e);
        String message = String.format("Malformed parameter: %s", e.getPropertyName());
        return new ResponseMessageDto(message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PropertyReferenceException.class)
    public final ResponseMessageDto handlePropertyReferenceException(
        PropertyReferenceException e) {
        log.debug("PropertyReferenceException", e);
        String message = String.format("Malformed sort parameter: %s", e.getPropertyName());
        return new ResponseMessageDto(message);
    }
}
