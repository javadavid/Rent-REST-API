package nz.co.zufang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nz.co.zufang.model.BasicUserLogin;
import nz.co.zufang.model.UserCreate;
import nz.co.zufang.service.UserService;
import nz.co.zufang.spec.TokenUtils;

/**
 * Created by Lillian on 2/25/2016.
 */
@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<GenericResponse> register(@RequestBody UserCreate userCreate) {
		GenericResponse response = userService.register(userCreate);
		return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<String> login(@RequestBody BasicUserLogin userLogin, Device device) {

		// Perform the authentication
		Authentication authentication = this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userLogin.getUserName(), userLogin.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Reload password post-authentication so we can generate token
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(userLogin.getUserName());
		String token = TokenUtils.generateToken(userDetails, device);

		// GenericResponse response =
		// userService.authentication(userLogin.getUserName(),
		// userLogin.getPassword());
		return new ResponseEntity<String>(token, HttpStatus.OK);
	}


}
