package br.com.gusthavo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.gusthavo.model.security.User;

public interface UserRepository extends JpaRepository<User, Long>{
	UserDetails findByLogin(String login);
}
