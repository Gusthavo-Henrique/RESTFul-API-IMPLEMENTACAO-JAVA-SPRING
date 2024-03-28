# RESTFul-API-IMPLEMENTACAO-SPRING
Implementação de uma API-RESTFul em Spring passo a passo.

<h3>Antes de iniciar o a implementação, é importante configurar o banco de dados que vai ser utilizado pela sua aplicação. 
Neste exemplo vai ser usado um banco de dados MySql 8.0.33 e ele vai ser configurado no arquivo application.yml do spring</h3>
<h5>Adicionar dependência do conector MySql</h5>

```bash
    <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <version>8.0.33</version>
    </dependency>
```
<h5>application.yml :</h5>

<h5>Adicionar dependências do JPA e Spring Web</h5>

```bash
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
```


```bash
spring:
  datasource:
    username: root
    password: 
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/productbooks
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
      dialect: MySQL8Dialect
```

<h2>PASSO 1: Definindo uma entidade</h2>
<p>Neste exemplo eu usarei uma entidade 'Books' com 4 atributos para um fácil entendimento</p>

```bash
@Entity
public class Books implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String author;
	private double price;

	public Books() {

	}
```
<p>A anotação do JPA @Entity especifica que a classe é uma entidade e a partir disso o JPA vai fazer uma relação no banco de dados dessa entidade em uma tabela.
 As anotações @Id e @GeneratedValue é utilizada para especificar qual atributo da classe é o 'ID' e de que jeito ele vai ser trabalhado/preenchido, que é definido no campo 'strategy' de @GeneratedValue.
No caso do exemplo acima o Id vai ser preenchido por auto incremento.</p>
<h5>Obs: Criar Construtores,Getters and Setters,HashCodes e implementar Serializable</h5>

<h2>PASSO 2:Criando Repositórios, Serviços e o Controller de Books</h2> 
<h3>Repositório JPA</h3>

```bash
@Repository
public interface BooksRepository extends JpaRepository<Books, Long>{

}
```
<p>Um repository é utilizado para abstrair os códigos de acesso a dados e tornar mais fácil as chamadas dos métodos. Ex: findById, Delete, Update etc...</p>
<p>Neste modelo usaremos o JpaRepository. É necessário marcar esta interface com a anotação @Repository para o Spring identificar que essa interface vai acessar ou manipular o banco de dados. 
Além disso é preciso passar a dóminio da entidade que vai ser acessada e o tipo de Id ex: String, Long etc...</p>
<h5>Obs: Você não precisa ficar preso apenas os métodos que já vem predefinido pelo JPA Repository, é possível criar várias funções personalizadas para a demanda da sua API</h5>
<h3>Services Books</h3>

```bash
@Service
public class BooksServices {
	
	@Autowired
	BooksRepository repository;
	
	public List<Books> findAll() {
		return repository.findAll();
	}
	
	public Books findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new BookIDNotFoundException("Book Id:" + id + "Not found"));
	}
	
	public Books insertBooks(Books book) {
		var newBook = repository.save(book);
		return newBook;
	}
	
	public void deleteBookById(Long id) {
		repository.deleteById(id);
	}
}
```
<p>Assim como na entidade e o repositório, a classe Service é marcada com a anotação @Service para indicar que é responsável por implementar os métodos que nossa API precisa retornar baseado no modelo de negócio definido.</p>
<p>Antes de criarmos os métodos, é preciso aplicar uma injeção de dependência em BooksRepository para que os métodos de serviços usem as funcionaliades do JpaRepository, essa injeção é feita com a anotação @Autowired.</p>
<p>Desse modo é só criar os serviços que fazem sentido no seu négocio. No exemplo foi criado 4 métodos: 'Encontrar Todos os livros' , 'Encontrar um livro pelo ID' , 'Inserir um novo livro' , 'Deletar um livro pelo ID'.</p>

<h2>Controller da API</h2>

```bash
@RestController
@RequestMapping("/Books")
public class BooksController {
	
	@Autowired
	BooksServices service;
	
	@GetMapping
	public ResponseEntity<List<Books>> findAll() {
		List<Books> listOfBooks = service.findAll();
		return ResponseEntity.ok().body(listOfBooks);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Books> findById(@PathVariable(name = "id") Long id) {
		Books foundedBook = service.findById(id);
		return ResponseEntity.ok().body(foundedBook);
	}
	
	@PostMapping
	public ResponseEntity<Books> insertBooks(@RequestBody Books book, UriComponentsBuilder uriBuilder) {
		Books newBook = service.insertBooks(book);
		URI uri = uriBuilder.path("/Books/{id}").buildAndExpand(book.getId()).toUri();
		return ResponseEntity.created(uri).body(newBook);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBookById(@PathVariable(name = "id") Long id) {
		service.deleteBookById(id);
		return ResponseEntity.ok().build();
	}
	
	
}
```
<p>O controller trata-se da classe que vai ser responsável por lidar com as requisições HTTP vindas da camada view do cliente. Ela é marcada com a anotação @RestController. É uma boa usar @RequestMapping('Seu-Path') para definir um
caminho padrão para o controller. Além disso temos que aplicar a injeção de dependência na classe de Serviços (BooksServices) que vai ser utilizada pelo controller para atender as requisições. </p>
<p>Os métodos do controller vao retornar um ResponseEntity que é basicamente uma resposta Http que você configura passando o serviço em um body.</p>

<h2>Implementação de HATEOAS (Hypermedia As the Engine Of Application State)</h2>
<p>É um modelo simples que uma API REST tem de fazer que quando consumida por alguma aplicação, o usuário possa navegar por links e URL, sem precisar ter
um conhecimento prévio do projeto.
</p>

```bash
import org.springframework.hateoas.RepresentationModel;

public class BooksDto extends RepresentationModel<BooksDto> implements Serializable {...}
```
<p>É preciso extender "RepresentationModel<E> na classe que vai suportar o Hateoas"</p>

```bash
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public List<BooksDto> findAll() {
	List<Books> books = repository.findAll();
	//Transformando Books em BooksDto
	List<BooksDto> booksDto = books.stream().map(p -> mapper.map(p, BooksDto.class)).toList();

	booksDto.forEach(p -> p.add(linkTo(methodOn(BooksController.class).findById(p.getId())).withSelfRel()));
	return booksDto;
}
```
<p>No método "findAll" da classe Services de Books foi passado um "forEach" e "add" para percorrer cada entidade que está na lista e adicionar
um trecho de código que faz com que quando for consumido o EndPoint "findAll" cada entidade retorne também o seu Link/URL. O LinkTo vai referenciar de onde
vai vir a URL e o methodOn vai referenciar o controller da entidade que vai ser retornada pelo link.</p>
<h4>A saída do JSON vai ficar assim: </h4>

```bash
[
    {
        "id": 1,
        "name": "The elder Scrolls",
        "author": "Bethesda",
        "price": 299.99,
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:8080/api/books/1"
            }
        ]
    },
    {
        "id": 2,
        "name": "As historias do aventureiro",
        "author": "Jorginho",
        "price": 129.99,
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:8080/api/books/2"
            }
        ]
    }
]
```

<h2>Adicionando JWT Security na API</h2>
<p>Usaremos o framework Spring Security para adicionarmos atenticação e geração de tokens à nossa API.</p>

```bash
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-test</artifactId>
</dependency>
		
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
<h3>Criando a classe do usuário e roles</h3>

```bash
@Entity
@Table(name = "Users")
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String password;

	@Enumerated(EnumType.STRING)
	private Role roles;

	public User() {

	}

	public User(String username, String password, Role roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(this.roles == Role.ADMIN) {
			return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
		}
		else {
			return List.of(new SimpleGrantedAuthority("ROLE_USER"));
		}
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
```
<p>A classe de usuário é uma entidade que vai implementar UserDetails que faz com que a classe que a estaja implementando represente um usuário de autenticação.
 Essa interface fornece 4 métodos booleanos que vão ser escritos como "True", e um GrantedAuthority que basicamente vai representar as permissões/autoridades que foi fornecida ao usuário autenticado, 
esse método vai retornar uma liste de SimpleGrantedAuthority definida pela sua regra de negócio.</p>

<h3>Repositório</h3>

```bash
package com.gusthavo.apiwebproject.Security.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.gusthavo.apiwebproject.Security.SecurityModel.User;

@Repository
public interface SecurityRepository extends JpaRepository<User,Long>{
    UserDetails findByUsername(String username);
}
```


<h3>AuthorizationService</h3>

```bash
package com.gusthavo.apiwebproject.Security.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gusthavo.apiwebproject.Security.Repository.SecurityRepository;

@Service
public class AuthorizationService implements UserDetailsService{

    @Autowired
    SecurityRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }
    
}
```

<h3>Token Service</h3>

```bash
package com.gusthavo.apiwebproject.Security.Services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.gusthavo.apiwebproject.Security.SecurityModel.User;

@Service
public class TokenService 
{
   @Value("${security.jwt.token.secret-key:secret}")
   private String secret; 

   @Value("${security.jwt.token.expire-length:3600000L}")
   private Long expireLength = 3600000L;

   public String generateToken(User user) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + expireLength);
    try {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create().withIssuer("meu-token").withSubject(user.getUsername()).withExpiresAt(validity).sign(algorithm);
        return token;
    } catch (JWTCreationException e) {
        throw new RuntimeException("Error while generated token ",e);
    }
   }

   public String validated(String token) {
    try {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm).withIssuer("meu-token").build().verify(token).getSubject();
    } catch (JWTVerificationException e) {
        return "";
    }
   }
}
```

<h3>Security Filter</h3>

```bash
package com.gusthavo.apiwebproject.Security.Filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gusthavo.apiwebproject.Security.Repository.SecurityRepository;
import com.gusthavo.apiwebproject.Security.Services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{

    @Autowired
    private TokenService service;

    @Autowired
    private SecurityRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = this.recoverToken(request);
        if(token != null) {
            var subject = service.validated(token);
            UserDetails user = repository.findByUsername(subject);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
    
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }

}
```
<h3>Security Configuration</h3>

```bash
package com.gusthavo.apiwebproject.Security.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gusthavo.apiwebproject.Security.Filter.SecurityFilter;

@Configuration
public class SecurityConfig {
    
    @Autowired
    SecurityFilter securityFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorized -> authorized
        .requestMatchers(HttpMethod.POST, "auth/login").permitAll()
        .requestMatchers(HttpMethod.POST, "auth/register").permitAll()
        .requestMatchers(HttpMethod.POST, "api/books").hasRole("ADMIN")
        .anyRequest().authenticated())
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
```

<h3>Controller da autenticação</h3>

```bash
package com.gusthavo.apiwebproject.Security.Controller;

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

import com.gusthavo.apiwebproject.Security.Dto.AuthenticationDto;
import com.gusthavo.apiwebproject.Security.Dto.LoginDto;
import com.gusthavo.apiwebproject.Security.Dto.RegisterDto;
import com.gusthavo.apiwebproject.Security.Repository.SecurityRepository;
import com.gusthavo.apiwebproject.Security.SecurityModel.User;
import com.gusthavo.apiwebproject.Security.Services.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SecurityRepository repository;

    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated AuthenticationDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated RegisterDto data) {
        if(repository.findByUsername(data.username()) != null) {
            return ResponseEntity.badRequest().build();
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.username(), encryptedPassword, data.roles());
        repository.save(newUser);

        return ResponseEntity.ok().build();
    }

}
```


