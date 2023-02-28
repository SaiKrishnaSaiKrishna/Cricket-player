package com.wipro.authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wipro.authentication.config.JwtService;
import com.wipro.authentication.model.Admin;
import com.wipro.authentication.model.ChangePassword;
import com.wipro.authentication.model.User;
import com.wipro.authentication.repository.UserRepository;

import com.wipro.authentication.model.*;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	
	@Override
	public String addUser(User user) {
		// TODO Auto-generated method stub
		if(repo.existsById(user.getEmail()))
			return null;
		else {
			User addUser = new User(user.getEmail(),passwordEncoder.encode(user.getPassword()),user.getFirstName(),user.getLastName());
			addUser.role = Role.USER;
			
			repo.save(addUser);
			System.out.println(addUser);
			String jwtToken = jwtService.generateToken(addUser);
			System.out.println(jwtToken);
			return jwtToken;
		}
	}

	@Override
	public User authenticateUser(Admin admin) {
		// TODO Auto-generated method stub
		
		Authentication authenticate;
		try {
			authenticate = authenticationManager.authenticate(
			    new UsernamePasswordAuthenticationToken(
			        admin.getEmail(),
			        admin.getPassword()
			    )
			);
			System.out.println(authenticate);
		} 
		catch (AuthenticationException e) {
			return null;
		}
	   
	    User user1 = repo.findById(admin.getEmail())
	        .orElseThrow();
//	    System.out.println(user1);
	    String jwtToken = jwtService.generateToken(user1);
	    user1.setToken(jwtToken);
//	    System.out.println(jwtToken);
	    return user1;
	}

	@Override
	public User changePassword(ChangePassword user) {
		// TODO Auto-generated method stub
		if (repo.existsById(user.getEmail())) {
			User dbUser = repo.findById(user.getEmail()).orElseThrow();
			User addUser = new User(user.getEmail(), passwordEncoder.encode(user.getNewPassword()),
					dbUser.getFirstName(), dbUser.getLastName());
			addUser.role = Role.USER;
			repo.save(addUser);
			return addUser;
		}
		else
			return null;
		
	}
	

}
