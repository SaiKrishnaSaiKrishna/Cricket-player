package com.wipro.authentication.service;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wipro.authentication.config.JwtService;
import com.wipro.authentication.model.Admin;
import com.wipro.authentication.model.ChangePassword;
import com.wipro.authentication.model.User;
import com.wipro.authentication.repository.UserRepository;






@SpringBootTest()
@TestInstance(Lifecycle.PER_CLASS)
public class UserServiceTest {
	
	@Mock
	UserRepository repo;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@Mock
	JwtService jwtService;
	
	@Mock
	AuthenticationManager authenticationManager;

	@InjectMocks
	UserServiceImpl service;
	
	
	List<User> users= new ArrayList<User>();
	
	Admin admin = new Admin("saibabu@gmail.com", "sai123");
	User sample = new User("saibabu@gmail.com", "sai123", "sai", "babu");
	User oldUser = new User("saibabu@gmail.com", "sai123", "sai", "babu");
	ChangePassword newUser = new ChangePassword("sai@gmail.com", "sai123","sai1432");
	
	@BeforeAll
	void initializations() {	
		users.add(new User("saibabu@gmail.com", "sai123", "sai", "babu"));
		users.add(new User("saikrishna@gmail.com", "saikrishna123", "sai", "krishna"));
		users.add(new User("sairam@gmail.com", "sairam123", "sai", "ram"));
	}
	
	
	@Test
	void testAddUserAlreadyExists() {
		when(repo.existsById(anyString())).thenReturn(true);
		assertNull(service.addUser(sample));
	}
	
	@Test
	void testAddUserNotExists() {
		when(repo.existsById(anyString())).thenReturn(false);	//assuming user not exists
		when(passwordEncoder.encode(anyString())).thenReturn("password");	//mocking password Encoder method
		when(repo.save(any(User.class))).thenReturn(sample);				//mocking repo save method
		when(jwtService.generateToken(any(User.class))).thenReturn("token");	//mocking jwt service method
		
		assertNotNull(service.addUser(sample));
	}
	
	@Test
	void testAuthenticateUserNull() {
		when(authenticationManager.
				authenticate(any(UsernamePasswordAuthenticationToken.class)))
		.thenReturn(null);
		
		Optional<User> opt = Optional.of(sample);
		
		when(repo.findById(anyString())).thenReturn(opt);
		when(jwtService.generateToken(any(User.class))).thenReturn("token");
	
		assertInstanceOf(User.class, service.authenticateUser(admin));
		assertNotNull(service.authenticateUser(admin));
		
		
	}
	
//	@Test
//	void testAuthenticateUserException() {
//		when(authenticationManager.
//				authenticate(any(UsernamePasswordAuthenticationToken.class)))
//		.thenThrow(AuthenticationException.class);
//		
//		assertNull(service.authenticateUser(admin));
//		
//		
//	}


	@Test
	void testChangePasswordAlreadyExists() {
		
		Optional<User> opt = Optional.of(oldUser);
		
		when(repo.existsById(anyString())).thenReturn(true);
		when(repo.findById(anyString())).thenReturn(opt);
		when(passwordEncoder.encode(anyString())).thenReturn("password");	//mocking password Encoder method
		when(repo.save(any(User.class))).thenReturn(sample);				//mocking repo save method
		
		assertNotNull(service.changePassword(newUser));
		assertInstanceOf(User.class, service.changePassword(newUser));
	}
	
	@Test
	void testChangePasswordNotExists() {
		when(repo.existsById(anyString())).thenReturn(false);
		assertNull(service.changePassword(newUser));
	}

}
