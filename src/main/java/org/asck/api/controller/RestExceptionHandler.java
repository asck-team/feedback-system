package org.asck.api.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.asck.api.exceptions.EntityNotFoundException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return buildResponseEntity(new ApiError(BAD_REQUEST, "Wrong type for parameter",ex));
	}
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = ex.getParameterName() + " parameter is missing";
		return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append(ex.getContentType());
		sb.append(" media type is not supported. Supported media types are ");
		sb.append(ex.getSupportedMediaTypes().stream().map(MediaType::toString).collect(Collectors.joining(",")));
		return buildResponseEntity(new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, sb.toString(), ex));
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
    }
	
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        return buildResponseEntity(new ApiError(NOT_FOUND, ex));
    }
    
    @ExceptionHandler(SQLGrammarException.class)
    protected ResponseEntity<Object> handleSQLGramarException(SQLGrammarException ex) {
    	return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
    		HttpHeaders headers, HttpStatus status, WebRequest request) {
    	ApiError apiError = new ApiError(BAD_REQUEST);
    	apiError.setMessage("Validation error");
    	apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
    	apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
    	return buildResponseEntity(apiError);
    }
    
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
    		HttpHeaders headers, HttpStatus status, WebRequest request) {
    	return buildResponseEntity(new ApiError(BAD_REQUEST, ex));
    }
    
	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
	
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstrainViolationException(ConstraintViolationException ex) {
		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getConstraintViolations());
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
	protected ResponseEntity<Object> handleSQLConstraintException(org.hibernate.exception.ConstraintViolationException e) {
		return buildResponseEntity(new ApiError(BAD_REQUEST, e));
	}

}
