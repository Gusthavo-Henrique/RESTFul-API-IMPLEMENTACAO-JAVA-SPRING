package br.com.gusthavo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gusthavo.model.Books;
import br.com.gusthavo.model.dto.BooksDto;
import br.com.gusthavo.services.BooksServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/books")
public class BooksController {

	@Autowired
	BooksServices services;

	@Operation(summary = "EndPoint Encontrar todoas", description = "Retornar todos os livros", tags = "{BooksDto}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucess", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BooksDto.class))) }), 
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Anauthorized", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
			})
	@GetMapping
	public ResponseEntity<List<BooksDto>> findAll() {
		List<BooksDto> allBooks = services.findAll();
		return ResponseEntity.ok().body(allBooks);
	}

	@Operation(summary = "EndPoint Encontrar por id", description = "Retornar todos os produtos por id", tags = "{BooksDto}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucess", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "404", description = "Not found",content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
	})
	@GetMapping("/{id}")
	public ResponseEntity<BooksDto> findById(@PathVariable(name = "id") Long id) {
		BooksDto book = services.findById(id);
		return ResponseEntity.ok().body(book);
	}
	
	@Operation(summary = "EndPoint Encontrar por id", description = "Retornar todos os produtos por id", tags = "{BooksDto}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucess", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "404", description = "Not found",content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
	})
	@PostMapping
	public ResponseEntity<BooksDto> insertBook(@RequestBody Books obj) {
		BooksDto book = services.insertBook(obj);
		return ResponseEntity.ok().body(book);
	}
	@Operation(summary = "EndPoint Encontrar por id", description = "Retornar todos os produtos por id", tags = "{BooksDto}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucess", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "404", description = "Not found",content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
	})
	@PutMapping("/{id}")
	public ResponseEntity<BooksDto> updateBook(@RequestBody Books obj, @PathVariable(name = "id") Long id) {
		BooksDto book = services.updateBook(obj);
		return ResponseEntity.ok().body(book);
	}
	@Operation(summary = "EndPoint Encontrar por id", description = "Retornar todos os produtos por id", tags = "{BooksDto}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "No content", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "404", description = "Not found",content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
		services.deleteBook(id);
		return ResponseEntity.noContent().build();
	}

}
