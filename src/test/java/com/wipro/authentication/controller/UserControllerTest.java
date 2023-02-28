package com.wipro.authentication.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.authentication.model.Admin;
import com.wipro.authentication.model.ChangePassword;
import com.wipro.authentication.model.Role;
import com.wipro.authentication.model.User;
import com.wipro.authentication.service.UserService;







@SpringBootTest()
@EnableWebMvc
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class UserControllerTest {
	
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	User savedUser;
	
	@BeforeAll
	void initialize() {
		savedUser = new User("saibabu@gmail.com", "sai123", "sai", "babu");
		savedUser.role= Role.USER;
	}
	
	@Test
	void testMethod() throws Exception {
		
		this.mockMvc.perform(get("/userservice/test"))
				.andExpect(status().isOk() );
	}
	
//	@Test
//	void testRegisterUser() throws Exception {
//		System.out.println("register");
//		when(service.addUser((User)any(User.class))).thenReturn("token");
//		
////		try {
////			RequestBuilder requestBuilder = MockMvcRequestBuilders
////			.post("/app/v1/register-user")
////			.contentType(MediaType.APPLICATION_JSON)
////			.content(objectMapper.writeValueAsString(savedUser));
//////			.contentType(MediaType.APPLICATION_JSON);
////			
////			MvcResult result= mockMvc.perform(requestBuilder).andReturn();
////			
////			MockHttpServletResponse response= result.getResponse();
////			
////			String outputJson = response.getContentAsString();
////			
////			assertThat(outputJson).isEqualTo("ok");
////			assertEquals(HttpStatus.OK.value(), response.getStatus());
////		} 
////		catch (ServletException e) {
////			e.printStackTrace();
////		}
//			
//		this.mockMvc.perform(post("userservice/register-user")
//		.contentType(MediaType.APPLICATION_JSON)
//
//		.content(objectMapper.writeValueAsString(savedUser)))
//		.andExpect(status().isOk());
////		
////		.andExpect(jsonPath("$.token", is("token")));
////		.andExpect(jsonPath("$.token", "token"));
//	}
	@Test
	void testAuthenticateUserNonNull() throws Exception {
		Admin user1= new Admin("saibabu@gmail.com", "sai123");
		when(service.authenticateUser(any(Admin.class))).thenReturn(savedUser);

		mockMvc.perform(post("/userservice/log-in")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user1)) )
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.email", is(savedUser.getEmail())))
		.andExpect(jsonPath("$.password", is(savedUser.getPassword())));
	
	}
	
	@Test
	void testAuthenticateUserNull() throws Exception {
		Admin user1= new Admin("saibabu@gmail.com", "sai123");
		when(service.authenticateUser(any(Admin.class))).thenReturn(null);

		mockMvc.perform(post("/userservice/log-in")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user1)) )
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.error", is("Wrong Credentials")));
	
	}

	@Test
	void testChangePasswordNonNull() throws JsonProcessingException, Exception {

		when(service.changePassword(any(ChangePassword.class))).thenReturn(savedUser);
		
		this.mockMvc.perform(post("/userservice/change-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new ChangePassword("saibabu@mail.com", "old", "new"))))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.message", is("Password Changed Successfully")));
	}
	
	@Test
	void testChangePasswordNull() throws JsonProcessingException, Exception {

		when(service.changePassword(any(ChangePassword.class))).thenReturn(null);
		
		this.mockMvc.perform(post("/userservice/change-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new ChangePassword("saibabu@mail.com", "old", "new"))))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message", is("Wrong Credentials")));
	}
		
	
	

}

