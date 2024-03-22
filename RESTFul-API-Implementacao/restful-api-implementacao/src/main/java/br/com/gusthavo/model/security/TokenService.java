package br.com.gusthavo.model.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenService {
	@Value("${security.jwt.token.secret-key:secret}")
	private String secret;

	@Value("${security.jwt.token.expire-length:3600000}")
	private Long expirelength = 3600000L;

	public String generateToken(User user) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + expirelength);
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token = JWT.create().withIssuer("Aula-Jwt").withSubject(user.getLogin()).withExpiresAt(validity)
					.sign(algorithm);
			return token;
		} catch (JWTCreationException exception) {
			throw new RuntimeException("Error while generated token ", exception);
		}
	}

	public String validated(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm).withIssuer("Aula-Jwt").build().verify(token).getSubject();
		} catch (JWTVerificationException exception) {
			return "";
		}
	}

}
