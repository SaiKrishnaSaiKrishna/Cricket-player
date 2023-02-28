package com.wipro.authentication.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.authentication.model.Admin;
import com.wipro.authentication.model.ChangePassword;
import com.wipro.authentication.model.User;
import com.wipro.authentication.service.UserService;



@CrossOrigin("*")
@RestController
@RequestMapping("/userservice")
public class UserController {
	
	@Autowired
	private UserService service;
	
//	Testing Method
	@GetMapping("/test")
	public ResponseEntity<HashMap<String, String>> testMethod() {
		HashMap<String, String> Value = new HashMap<String, String>();
		Value.put("test", "Got the request!");
		return new ResponseEntity<HashMap<String, String>>(Value, HttpStatus.OK);
	}
	
//	Registering User
	@PostMapping("/register-user")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		System.out.println("controller");
		HashMap<String, String> Value = new HashMap<String, String>();
		String token = service.addUser(user);
		if(token != null) {
			Value.put("token", token);
			return new ResponseEntity<>(Value, HttpStatus.OK);
		}
		else {
			Value.put("error", "User with Email Already Exists!!");
			return new ResponseEntity<>(Value, HttpStatus.BAD_REQUEST);
		}
	}
	
//	Log in User
	@PostMapping("/log-in")
	public ResponseEntity<?> authenticateUser(@RequestBody Admin admin) {
		System.out.println("request");
		HashMap<String, String> Value = new HashMap<String, String>();
		User loggedUser = service.authenticateUser(admin);
		if(loggedUser==null) {
			Value.put("error", "Wrong Credentials");
			return new ResponseEntity<>(Value, HttpStatus.BAD_REQUEST);
		}
		else {
			return new ResponseEntity<>(loggedUser, HttpStatus.OK);
		}
	}

//changePassword
	@PostMapping("/change-password")
	public ResponseEntity<HashMap<String, String>> changePassword(@RequestBody ChangePassword user) {
		HashMap<String, String> Value = new HashMap<String, String>();
		User user1 = service.changePassword(user);
		if(user1==(null)) {
			Value.put("message", "Wrong Credentials");
			return new ResponseEntity<>(Value, HttpStatus.BAD_REQUEST);
		}
			
		else {
			Value.put("message", "Password Changed Successfully");
			return new ResponseEntity<>(Value, HttpStatus.OK);
		}
	}
	
	

}
