package br.com.gusthavo.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundBookByID extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public NotFoundBookByID(String msg) {
		super(msg);
	}
	
}
