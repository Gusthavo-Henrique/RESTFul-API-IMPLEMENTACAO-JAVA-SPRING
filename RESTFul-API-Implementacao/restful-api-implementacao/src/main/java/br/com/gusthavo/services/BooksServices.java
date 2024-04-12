package br.com.gusthavo.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.gusthavo.controller.BooksController;
import br.com.gusthavo.model.Books;
import br.com.gusthavo.model.dto.BooksDto;
import br.com.gusthavo.repository.BooksRepository;
import br.com.gusthavo.services.exceptions.NotFoundBookByID;

@Service
public class BooksServices {

	@Autowired
	BooksRepository repository;

	ModelMapper mapper = new ModelMapper();

	public List<BooksDto> findAll() {
		List<Books> books = repository.findAll();
		List<BooksDto> booksDto = books.stream().map(p -> mapper.map(p, BooksDto.class)).toList();
		booksDto.forEach(p -> p.add(linkTo(methodOn(BooksController.class).findById(p.getId())).withSelfRel()));
		return booksDto;
	}

	public BooksDto findById(Long id) {
		Books book = repository.findById(id)
				.orElseThrow(() -> new NotFoundBookByID("Livro com o id: " + id + " não encontrado"));
		BooksDto bookdto = mapper.map(book, BooksDto.class);
		bookdto.add(linkTo(methodOn(BooksController.class).findById(id)).withSelfRel());
		return bookdto;
	}

	public BooksDto insertBook(Books obj) {
		Books book = repository.save(obj);
		BooksDto bookDto = mapper.map(book, BooksDto.class);
		bookDto.add(linkTo(methodOn(BooksController.class).findById(obj.getId())).withSelfRel());
		return bookDto;
	}

	public BooksDto updateBook(Books obj) {
		Books book = repository.findById(obj.getId())
				.orElseThrow(() -> new NotFoundBookByID("Livro com o id: " + obj.getId() + " não encontrado"));
		book.setId(obj.getId());
		book.setName(obj.getName());
		book.setAuthor(obj.getAuthor());
		book.setPrice(obj.getPrice());
		repository.save(book);
		BooksDto bookDto = mapper.map(book, BooksDto.class);
		bookDto.add(linkTo(methodOn(BooksController.class).findById(obj.getId())).withSelfRel());
		return bookDto;
	}

	public void deleteBook(Long id) {
		repository.deleteById(id);
	}
}
