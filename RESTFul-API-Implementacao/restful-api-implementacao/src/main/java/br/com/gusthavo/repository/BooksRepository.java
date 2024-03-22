package br.com.gusthavo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gusthavo.model.Books;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long>{

}
