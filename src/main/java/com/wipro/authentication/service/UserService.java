package com.wipro.authentication.service;

import org.springframework.stereotype.Service;

import com.wipro.authentication.model.Admin;
import com.wipro.authentication.model.ChangePassword;
import com.wipro.authentication.model.User;
@Service
public interface UserService {
	
	public String addUser(User user);
	public User authenticateUser(Admin admin);
	public User changePassword(ChangePassword user);

}
