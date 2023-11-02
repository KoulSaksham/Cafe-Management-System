package com.spring.CMS.Rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.CMS.Wrapper.UserWrapper;
@CrossOrigin
@RequestMapping(path = "/user")
public interface UserRest {
	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);

	@GetMapping(path = "/get")
	public ResponseEntity<List<UserWrapper>> getAllUsers();

	@PostMapping(path = "/update")
	public ResponseEntity<String> update(@RequestBody(required = true) Map<String, String> requestMap);

	@GetMapping(path = "/checkToken")
	ResponseEntity<String> checkToken();

	@PostMapping(path = "/changePassword")
	ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap);

	@PostMapping(path = "/forgotPassword")
	ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestMap);
}
