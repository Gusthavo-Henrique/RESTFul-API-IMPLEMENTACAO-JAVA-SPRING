package br.com.gusthavo.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.gusthavo.services.exceptions.NotFoundBookByID;

@RestControllerAdvice
public class ExceptionHandlerClass extends ResponseEntityExceptionHandler{

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandardException> globalExceptions(WebRequest request, RuntimeException ex) {
		StandardException error = new StandardException(new Date(), ex.getMessage(), request.getDescription(true));
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(NotFoundBookByID.class)
	public ResponseEntity<StandardException> notFoundBookByID(WebRequest request, RuntimeException ex) {
		StandardException error = new StandardException(new Date(), ex.getMessage(), request.getDescription(true));
		return new ResponseEntity<StandardException>(error, HttpStatus.NOT_FOUND);
	}
	
}
