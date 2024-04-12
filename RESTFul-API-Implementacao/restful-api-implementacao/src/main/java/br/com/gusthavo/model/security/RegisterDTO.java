package br.com.gusthavo.model.security;

public record RegisterDTO(Long id,String login, String password, UserRole role) {

}
