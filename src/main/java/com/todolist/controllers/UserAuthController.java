package com.todolist.controllers;

import com.todolist.configurations.JwtTokenUtil;
import com.todolist.dto.UserAuthResponse;
import com.todolist.dto.UserDto;
import com.todolist.services.TodoServiceImpl;
import com.todolist.services.UserService;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserAuthController {

	@Autowired
	private JwtTokenUtil jwtoTokenUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;
	static final Logger logger = Logger.getLogger(TodoServiceImpl.class.getName());

	@PostMapping("/login")
	public ResponseEntity<UserAuthResponse> createToken(@RequestBody UserDto request) throws Exception {
		UserAuthResponse response = new UserAuthResponse();

		try {

			logger.log(Level.INFO, "authenticating user using authetication provider...");

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					request.getUserName(), request.getPassword());
			this.authenticationManager.authenticate(authToken);
		} catch (AuthenticationException e) {
			logger.log(Level.SEVERE, "user authentication fail due to incorrect user or password...");
			response.setJwtToken("");
			response.setStatus(false);
			response.setMessage("Login Fail");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		UserDetails userDetails = this.userService.loadUserByUsername(request.getUserName());
		logger.log(Level.INFO, "Generating jwt token...");

		String token = this.jwtoTokenUtil.generateToken(userDetails);

		response.setJwtToken(token);
		response.setStatus(true);
		response.setMessage("Login Success");
		
		logger.log(Level.INFO, "sending response to the user...");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
