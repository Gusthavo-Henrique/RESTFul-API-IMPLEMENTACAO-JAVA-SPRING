package br.com.gusthavo.model.dto;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import br.com.gusthavo.model.Books;

public class BooksDto extends RepresentationModel<BooksDto> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String author;
	private double price;

	public BooksDto() {

	}

	public BooksDto(Books book) {
		this.id = book.getId();
		this.name = book.getName();
		this.author = book.getAuthor();
		this.price = book.getPrice();
	}

	public BooksDto(Long id, String name, String author, double price) {
		this.id = id;
		this.name = name;
		this.author = author;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}