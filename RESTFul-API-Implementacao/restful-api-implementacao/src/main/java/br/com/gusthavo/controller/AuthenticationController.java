package br.com.gusthavo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gusthavo.model.security.AuthenticationDTO;
import br.com.gusthavo.model.security.RegisterDTO;
import br.com.gusthavo.model.security.TokenService;
import br.com.gusthavo.model.security.User;
import br.com.gusthavo.model.security.loginDTO;
import br.com.gusthavo.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	TokenService services;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Validated AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(),data.password());
		var auth = this.authManager.authenticate(usernamePassword);
		var token = services.generateToken( (User) auth.getPrincipal());
		return ResponseEntity.ok(new loginDTO(token));
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody @Validated RegisterDTO data) {
		if (repository.findByLogin(data.login()) != null) {
			return ResponseEntity.badRequest().build();
		}
		String encrypitedPassword = new BCryptPasswordEncoder().encode(data.password());
		User newUser = new User(data.login(), encrypitedPassword, data.role());
		repository.save(newUser);
		
		return ResponseEntity.ok().build();
	}
	
}
